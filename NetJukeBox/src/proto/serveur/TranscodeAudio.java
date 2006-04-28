package proto.serveur;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.media.Codec;
import javax.media.ConfigureCompleteEvent;
import javax.media.Control;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.MediaTimeSetEvent;
import javax.media.Owned;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.StopAtTimeEvent;
import javax.media.Time;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import com.sun.media.format.WavAudioFormat;

/**
 * A sample program to transcode an input source to an output location with
 * different data formats.
 */
public class TranscodeAudio implements ControllerListener, DataSinkListener {

	/**
	 * Given a source media locator, destination media locator and an array of
	 * formats, this method will transcode the source to the dest into the
	 * specified formats.
	 */
	public boolean doIt(MediaLocator inML, MediaLocator outML, Format fmts[],
			int start, int end) {

		Processor p;

		try {
			System.err.println("- create processor for: " + inML);
			p = Manager.createProcessor(inML);
		} catch (Exception e) {
			System.err
					.println("Yikes!  Cannot create a processor from the given url: "
							+ e);
			return false;
		}

		p.addControllerListener(this);

		// Put the Processor into configured state.
		p.configure();
		if (!waitForState(p, p.Configured)) {
			System.err.println("Failed to configure the processor.");
			return false;
		}

		// Set the output content descriptor based on the media locator.
		setContentDescriptor(p, outML);

		// Program the tracks to the given output formats.
		if (!setTrackFormats(p, fmts))
			return false;

		// We are done with programming the processor. Let's just
		// realize the it.
		p.realize();
		if (!waitForState(p, p.Realized)) {
			System.err.println("Failed to realize the processor.");
			return false;
		}
		
		// Now, we'll need to create a DataSink.
		DataSink dsink;
		if ((dsink = createDataSink(p, outML)) == null) {
			System.err
					.println("Failed to create a DataSink for the given output MediaLocator: "
							+ outML);
			return false;
		}

		dsink.addDataSinkListener(this);
		fileDone = false;

		// Set the start time if there's one set.
		if (start > 0)
			p.setMediaTime(new Time((double) start));

		// Set the stop time if there's one set.
		if (end > 0)
			p.setStopTime(new Time((double) end));

		System.err.println("start transcoding...");

		// OK, we can now start the actual transcoding.
		try {
			p.start();
			dsink.start();
		} catch (IOException e) {
			System.err.println("IO error during transcoding");
			return false;
		}

		// Wait for EndOfStream event.
		waitForFileDone();

		// Cleanup.
		try {
			dsink.close();
		} catch (Exception e) {
		}
		p.removeControllerListener(this);

		System.err.println("...done transcoding.");

		return true;
	}

	/**
	 * Set the content descriptor based on the given output MediaLocator.
	 */
	void setContentDescriptor(Processor p, MediaLocator outML) {

		ContentDescriptor cd;

		// If the output file maps to a content type,
		// we'll try to set it on the processor.

		if ((cd = fileExtToCD(outML.getRemainder())) != null) {

			System.err.println("- set content descriptor to: " + cd);

			if ((p.setContentDescriptor(cd)) == null) {

				// The processor does not support the output content
				// type. But we can set the content type to RAW and
				// see if any DataSink supports it.

				p.setContentDescriptor(new ContentDescriptor(
						ContentDescriptor.RAW));
			}
		}
	}

	/**
	 * Set the target transcode format on the processor.
	 */
	boolean setTrackFormats(Processor p, Format fmts[]) {

		if (fmts.length == 0)
			return true;

		TrackControl tcs[];

		if ((tcs = p.getTrackControls()) == null) {
			// The processor does not support any track control.
			System.err
					.println("The Processor cannot transcode the tracks to the given formats");
			return false;
		}

		for (int i = 0; i < fmts.length; i++) {

			System.err.println("- set track format to: " + fmts[i]);

			if (!setEachTrackFormat(p, tcs, fmts[i])) {
				System.err.println("Cannot transcode any track to: " + fmts[i]);
				return false;
			}
		}

		return true;
	}

	/**
	 * We'll loop through the tracks and try to find a track that can be
	 * converted to the given format.
	 */
	boolean setEachTrackFormat(Processor p, TrackControl tcs[], Format fmt) {

		Format supported[];
		Format f;

		for (int i = 0; i < tcs.length; i++) {

			supported = tcs[i].getSupportedFormats();

			if (supported == null)
				continue;

			for (int j = 0; j < supported.length; j++) {

				if (fmt.matches(supported[j])
						&& (f = fmt.intersects(supported[j])) != null
						&& tcs[i].setFormat(f) != null) {

					// Success.
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Create the DataSink.
	 */
	DataSink createDataSink(Processor p, MediaLocator outML) {

		DataSource ds;

		if ((ds = p.getDataOutput()) == null) {
			System.err
					.println("Something is really wrong: the processor does not have an output DataSource");
			return null;
		}

		DataSink dsink;

		try {
			System.err.println("- create DataSink for: " + outML);
			dsink = Manager.createDataSink(ds, outML);
			dsink.open();
		} catch (Exception e) {
			System.err.println("Cannot create the DataSink: " + e);
			return null;
		}

		return dsink;
	}

	Object waitSync = new Object();

	boolean stateTransitionOK = true;

	/**
	 * Block until the processor has transitioned to the given state. Return
	 * false if the transition failed.
	 */
	boolean waitForState(Processor p, int state) {
		synchronized (waitSync) {
			try {
				while (p.getState() < state && stateTransitionOK)
					waitSync.wait();
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}

	/**
	 * Controller Listener.
	 */
	public void controllerUpdate(ControllerEvent evt) {

		if (evt instanceof ConfigureCompleteEvent
				|| evt instanceof RealizeCompleteEvent
				|| evt instanceof PrefetchCompleteEvent) {
			synchronized (waitSync) {
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		} else if (evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (evt instanceof EndOfMediaEvent) {
			evt.getSourceController().close();
		} else if (evt instanceof MediaTimeSetEvent) {
			System.err.println("- mediaTime set: "
					+ ((MediaTimeSetEvent) evt).getMediaTime().getSeconds());
		} else if (evt instanceof StopAtTimeEvent) {
			System.err.println("- stop at time: "
					+ ((StopAtTimeEvent) evt).getMediaTime().getSeconds());
			evt.getSourceController().close();
		}
	}

	Object waitFileSync = new Object();

	boolean fileDone = false;

	boolean fileSuccess = true;

	/**
	 * Block until file writing is done.
	 */
	boolean waitForFileDone() {
		System.err.print("  ");
		synchronized (waitFileSync) {
			try {
				while (!fileDone) {
					waitFileSync.wait(1000);
					System.err.print(".");
				}
			} catch (Exception e) {
			}
		}
		System.err.println("");
		return fileSuccess;
	}

	/**
	 * Event handler for the file writer.
	 */
	public void dataSinkUpdate(DataSinkEvent evt) {

		if (evt instanceof EndOfStreamEvent) {
			synchronized (waitFileSync) {
				fileDone = true;
				waitFileSync.notifyAll();
			}
		} else if (evt instanceof DataSinkErrorEvent) {
			synchronized (waitFileSync) {
				fileDone = true;
				fileSuccess = false;
				waitFileSync.notifyAll();
			}
		}
	}

	/**
	 * Convert a file name to a content type. The extension is parsed to
	 * determine the content type.
	 */
	ContentDescriptor fileExtToCD(String name) {

		String ext;
		int p;

		// Extract the file extension.
		if ((p = name.lastIndexOf('.')) < 0)
			return null;

		ext = (name.substring(p + 1)).toLowerCase();

		String type;

		// Use the MimeManager to get the mime type from the file extension.
		if (ext.equals("mp3")) {
			type = FileTypeDescriptor.MPEG_AUDIO;
		} else {
			if ((type = com.sun.media.MimeManager.getMimeType(ext)) == null)
				return null;
			type = ContentDescriptor.mimeTypeToPackageName(type);
		}

		return new FileTypeDescriptor(type);
	}

	/**
	 * Create a media locator from the given string.
	 */
	static MediaLocator createMediaLocator(String url) {

		MediaLocator ml;

		if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null)
			return ml;

		if (url.startsWith(File.separator)) {
			if ((ml = new MediaLocator("file:" + url)) != null)
				return ml;
		} else {
			String file = "file:" + System.getProperty("user.dir")
					+ File.separator + url;
			if ((ml = new MediaLocator(file)) != null)
				return ml;
		}

		return null;
	}

	/**
	 * Parse the audio format specifier and generate an AudioFormat. A valid
	 * audio format specifier is of the form:
	 * [encoding]:[rate]:[sizeInBits]:[channels]:[big|little]:[signed|unsigned]
	 */
	static Format parseAudioFormat(String fmtStr) {

		int rate, bits, channels, endian, signed;

		String encodeStr = null, rateStr = null, bitsStr = null, channelsStr = null, endianStr = null, signedStr = null;

		// Parser the media locator to extract the requested format.

		if (fmtStr != null && fmtStr.length() > 0) {
			while (fmtStr.length() > 1 && fmtStr.charAt(0) == ':')
				fmtStr = fmtStr.substring(1);

			// Now see if there's a encode rate specified.
			int off = fmtStr.indexOf(':');
			if (off == -1) {
				if (!fmtStr.equals(""))
					encodeStr = fmtStr;
			} else {
				encodeStr = fmtStr.substring(0, off);
				fmtStr = fmtStr.substring(off + 1);
				// Now see if there's a sample rate specified
				off = fmtStr.indexOf(':');
				if (off == -1) {
					if (!fmtStr.equals(""))
						rateStr = fmtStr;
				} else {
					rateStr = fmtStr.substring(0, off);
					fmtStr = fmtStr.substring(off + 1);
					// Now see if there's a size specified
					off = fmtStr.indexOf(':');
					if (off == -1) {
						if (!fmtStr.equals(""))
							bitsStr = fmtStr;
					} else {
						bitsStr = fmtStr.substring(0, off);
						fmtStr = fmtStr.substring(off + 1);
						// Now see if there's channels specified.
						off = fmtStr.indexOf(':');
						if (off == -1) {
							if (!fmtStr.equals(""))
								channelsStr = fmtStr;
						} else {
							channelsStr = fmtStr.substring(0, off);
							fmtStr = fmtStr.substring(off + 1);
							// Now see if there's endian specified.
							off = fmtStr.indexOf(':');
							if (off == -1) {
								if (!fmtStr.equals(""))
									endianStr = fmtStr.substring(off + 1);
							} else {
								endianStr = fmtStr.substring(0, off);
								if (!fmtStr.equals(""))
									signedStr = fmtStr.substring(off + 1);
							}
						}
					}
				}
			}
		}

		// Sample Rate
		rate = AudioFormat.NOT_SPECIFIED;
		if (rateStr != null) {
			try {
				Integer integer = Integer.valueOf(rateStr);
				if (integer != null)
					rate = integer.intValue();
			} catch (Throwable t) {
			}
		}

		// Sample Size
		bits = AudioFormat.NOT_SPECIFIED;
		if (bitsStr != null) {
			try {
				Integer integer = Integer.valueOf(bitsStr);
				if (integer != null)
					bits = integer.intValue();
			} catch (Throwable t) {
			}
		}

		// # of channels
		channels = AudioFormat.NOT_SPECIFIED;
		if (channelsStr != null) {
			try {
				Integer integer = Integer.valueOf(channelsStr);
				if (integer != null)
					channels = integer.intValue();
			} catch (Throwable t) {
			}
		}

		// Endian
		endian = AudioFormat.NOT_SPECIFIED;
		if (endianStr != null) {
			if (endianStr.equalsIgnoreCase("big"))
				endian = AudioFormat.BIG_ENDIAN;
			else if (endianStr.equalsIgnoreCase("little"))
				endian = AudioFormat.LITTLE_ENDIAN;
		}

		// Signed
		signed = AudioFormat.NOT_SPECIFIED;
		if (signedStr != null) {
			if (signedStr.equalsIgnoreCase("signed"))
				signed = AudioFormat.SIGNED;
			else if (signedStr.equalsIgnoreCase("unsigned"))
				signed = AudioFormat.UNSIGNED;
		}

		return new AudioFormat(encodeStr, rate, bits, channels, endian, signed);
	}
	
	/**
	 * Main program
	 */
	public TranscodeAudio(String input, String output, String audioFormat) {

		String inputURL = null, outputURL = null;
		int mediaStart = -1, mediaEnd = -1;
		Vector audFmt = new Vector(), vidFmt = new Vector();

		// Parse the arguments.
			audFmt.addElement(audioFormat);
			outputURL = output;
			inputURL = input;

		int j = 0;
		Format fmts[] = new Format[audFmt.size() + vidFmt.size()];
		Format fmt;

		// Parse the audio format spec. into real AudioFormat's.
		for (int i = 0; i < audFmt.size(); i++) {

			if ((fmt = parseAudioFormat((String) audFmt.elementAt(i))) == null) {
				System.err.println("Invalid audio format specification: "
						+ (String) audFmt.elementAt(i));
				prUsage();
			}
			fmts[j++] = fmt;
		}

		// Generate the input and output media locators.
		MediaLocator iml, oml;

		if ((iml = createMediaLocator(inputURL)) == null) {
			System.err.println("Cannot build media locator from: " + inputURL);
			System.exit(0);
		}

		if ((oml = createMediaLocator(outputURL)) == null) {
			System.err.println("Cannot build media locator from: " + outputURL);
			System.exit(0);
		}

		if (!doIt(iml, oml, fmts, mediaStart, mediaEnd)) {
			System.err.println("Transcoding failed");
		}

		System.exit(0);
	}
	

	static void prUsage() {
		System.err
				.println("Usage: java Transcode -o <output> -a <audio format> -v <video format> -s <start time> -e <end time> <input>");
		System.err.println("     <output>: input URL or file name");
		System.err.println("     <input>: output URL or file name");
		System.err
				.println("     <audio format>: [encoding]:[rate]:[sizeInBits]:[channels]:[big|little]:[signed|unsigned]");
		System.err.println("     <video format>: [encoding]:[widthXheight]");
		System.err.println("     <start time>: start time in seconds");
		System.err.println("     <end time>: end time in seconds");
		System.exit(0);
	}
}
