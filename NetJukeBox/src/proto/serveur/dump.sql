--
-- PostgreSQL database dump
--

SET client_encoding = 'LATIN1';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: annuaire; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE annuaire (
    nom character varying,
    prenom character varying,
    tel character varying
);


ALTER TABLE public.annuaire OWNER TO postgres;

--
-- Name: attribuer; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE attribuer (
    id_perm integer NOT NULL,
    login character(6) NOT NULL
);


ALTER TABLE public.attribuer OWNER TO postgres;

SET default_with_oids = true;

--
-- Name: canal; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE canal (
    id serial NOT NULL,
    nom character varying NOT NULL,
    utilmax integer DEFAULT 0
);


ALTER TABLE public.canal OWNER TO postgres;

--
-- Name: canal_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('canal', 'id'), 2, true);


SET default_with_oids = false;

--
-- Name: contractant; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE contractant (
    id_contrat integer NOT NULL,
    nom_contrat character(30),
    adr_contrat character(80),
    date_contrat date,
    id_doc integer,
    duree_contrat integer
);


ALTER TABLE public.contractant OWNER TO postgres;

SET default_with_oids = true;

--
-- Name: document; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE document (
    id serial NOT NULL,
    titre character varying NOT NULL,
    duree integer,
    date character varying,
    source character varying,
    langue character varying,
    genre character varying,
    fichier character varying NOT NULL
);


ALTER TABLE public.document OWNER TO postgres;

--
-- Name: document_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('document', 'id'), 2, true);


SET default_with_oids = false;

--
-- Name: etat; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE etat (
    id_etat character(2) NOT NULL,
    lib_etat character(10)
);


ALTER TABLE public.etat OWNER TO postgres;

--
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE genre (
    id_genre integer NOT NULL,
    lib_genre character(30)
);


ALTER TABLE public.genre OWNER TO postgres;

--
-- Name: gerer; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE gerer (
    id_canal character(6) NOT NULL,
    login character(6) NOT NULL
);


ALTER TABLE public.gerer OWNER TO postgres;

--
-- Name: journal; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE journal (
    id_journal integer NOT NULL,
    date_journal date
);


ALTER TABLE public.journal OWNER TO postgres;

--
-- Name: langue; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE langue (
    id_langue integer NOT NULL,
    libel_langue character(30)
);


ALTER TABLE public.langue OWNER TO postgres;

--
-- Name: permission; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE permission (
    id_perm integer NOT NULL,
    lib_perm character(60)
);


ALTER TABLE public.permission OWNER TO postgres;

SET default_with_oids = true;

--
-- Name: programmation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programmation (
    id_prog integer NOT NULL,
    id_doc integer NOT NULL,
    calage bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.programmation OWNER TO postgres;

--
-- Name: programme; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programme (
    id serial NOT NULL,
    titre character varying NOT NULL,
    thematique character varying,
    duree bigint DEFAULT 0
);


ALTER TABLE public.programme OWNER TO postgres;

--
-- Name: programme_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('programme', 'id'), 1, true);


SET default_with_oids = false;

--
-- Name: protocole; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE protocole (
    id_proto integer NOT NULL,
    lib_proto character(30),
    detail_proto character(30)
);


ALTER TABLE public.protocole OWNER TO postgres;

--
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE utilisateur (
    log character(6) NOT NULL,
    nom character(25),
    premon character(25),
    mail character(25),
    pwd character(6),
    pays character(25)
);


ALTER TABLE public.utilisateur OWNER TO postgres;

--
-- Data for Name: annuaire; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO annuaire VALUES ('pipi', 'pépé', '111111');
INSERT INTO annuaire VALUES ('pipi', 'pépé', '111111');


--
-- Data for Name: attribuer; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: canal; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO canal VALUES (1, 'classic', 10);
INSERT INTO canal VALUES (2, 'jazz', 20);


--
-- Data for Name: contractant; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO document VALUES (1, 'Rhaspody in Blue', 1200, '01-01-2006', 'Philippe', 'FR', 'classic', 'file://home/philippe/njb/1.mp3');
INSERT INTO document VALUES (2, 'test', 10, '02-03-2005', 'Dom', 'EN', 'jazz', 'file://home/philippe/njb/2.mp3');


--
-- Data for Name: etat; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: genre; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: gerer; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: journal; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: langue; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: programmation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO programmation VALUES (1, 1, 1200);


--
-- Data for Name: programme; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO programme VALUES (1, 'prog1', 'classic', 1200);


--
-- Data for Name: protocole; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: utilisateur; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: id_doc; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY document
    ADD CONSTRAINT id_doc PRIMARY KEY (id);


ALTER INDEX public.id_doc OWNER TO postgres;

--
-- Name: id_prog; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programme
    ADD CONSTRAINT id_prog PRIMARY KEY (id);


ALTER INDEX public.id_prog OWNER TO postgres;

--
-- Name: pk_attribuer; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY attribuer
    ADD CONSTRAINT pk_attribuer PRIMARY KEY (id_perm, login);


ALTER INDEX public.pk_attribuer OWNER TO postgres;

--
-- Name: pk_contractant; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY contractant
    ADD CONSTRAINT pk_contractant PRIMARY KEY (id_contrat);


ALTER INDEX public.pk_contractant OWNER TO postgres;

--
-- Name: pk_etat; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY etat
    ADD CONSTRAINT pk_etat PRIMARY KEY (id_etat);


ALTER INDEX public.pk_etat OWNER TO postgres;

--
-- Name: pk_genre; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY genre
    ADD CONSTRAINT pk_genre PRIMARY KEY (id_genre);


ALTER INDEX public.pk_genre OWNER TO postgres;

--
-- Name: pk_gerer; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY gerer
    ADD CONSTRAINT pk_gerer PRIMARY KEY (id_canal, login);


ALTER INDEX public.pk_gerer OWNER TO postgres;

--
-- Name: pk_journal; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY journal
    ADD CONSTRAINT pk_journal PRIMARY KEY (id_journal);


ALTER INDEX public.pk_journal OWNER TO postgres;

--
-- Name: pk_langue; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY langue
    ADD CONSTRAINT pk_langue PRIMARY KEY (id_langue);


ALTER INDEX public.pk_langue OWNER TO postgres;

--
-- Name: pk_permission; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT pk_permission PRIMARY KEY (id_perm);


ALTER INDEX public.pk_permission OWNER TO postgres;

--
-- Name: pk_protocole; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY protocole
    ADD CONSTRAINT pk_protocole PRIMARY KEY (id_proto);


ALTER INDEX public.pk_protocole OWNER TO postgres;

--
-- Name: pk_utilisateur; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY utilisateur
    ADD CONSTRAINT pk_utilisateur PRIMARY KEY (log);


ALTER INDEX public.pk_utilisateur OWNER TO postgres;

--
-- Name: fk_attribuer_2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY attribuer
    ADD CONSTRAINT fk_attribuer_2 FOREIGN KEY (id_perm) REFERENCES permission(id_perm);


--
-- Name: fk_attribuer_3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY attribuer
    ADD CONSTRAINT fk_attribuer_3 FOREIGN KEY (login) REFERENCES utilisateur(log);


--
-- Name: fk_gerer_2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY gerer
    ADD CONSTRAINT fk_gerer_2 FOREIGN KEY (login) REFERENCES utilisateur(log);


--
-- Name: id_doc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programmation
    ADD CONSTRAINT id_doc FOREIGN KEY (id_doc) REFERENCES document(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: id_prog; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programmation
    ADD CONSTRAINT id_prog FOREIGN KEY (id_prog) REFERENCES programme(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

