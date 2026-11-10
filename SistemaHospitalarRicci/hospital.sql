--
-- PostgreSQL database dump
--

-- Dumped from database version 13.3
-- Dumped by pg_dump version 13.3

-- Started on 2026-11-10 16:12:42

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 74898)
-- Name: consultas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consultas (
    id bigint NOT NULL,
    crm_medico character varying(20) NOT NULL,
    cpf_paciente character varying(11) NOT NULL,
    data_consulta date NOT NULL,
    realizada boolean DEFAULT false NOT NULL
);


ALTER TABLE public.consultas OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 74896)
-- Name: consultas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.consultas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.consultas_id_seq OWNER TO postgres;

--
-- TOC entry 3045 (class 0 OID 0)
-- Dependencies: 202
-- Name: consultas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.consultas_id_seq OWNED BY public.consultas.id;


--
-- TOC entry 206 (class 1259 OID 74934)
-- Name: consultas_realizadas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consultas_realizadas (
    id_consulta bigint NOT NULL,
    crm_medico character varying(20) NOT NULL,
    cpf_paciente character varying(11) NOT NULL,
    data_consulta date NOT NULL,
    valor_consulta numeric(10,2) DEFAULT 0 NOT NULL,
    sintomas text,
    tratamento text,
    exames text,
    medicamentos text,
    texto_avaliacao text DEFAULT ''::text NOT NULL,
    estrelas smallint DEFAULT 0 NOT NULL,
    CONSTRAINT ck_consultas_realizadas_estrelas CHECK (((estrelas >= 0) AND (estrelas <= 5))),
    CONSTRAINT ck_consultas_realizadas_valor CHECK ((valor_consulta >= (0)::numeric))
);


ALTER TABLE public.consultas_realizadas OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 74917)
-- Name: lista_espera; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lista_espera (
    id bigint NOT NULL,
    crm_medico character varying(20) NOT NULL,
    cpf_paciente character varying(11) NOT NULL,
    data_consulta date NOT NULL,
    criada_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.lista_espera OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 74915)
-- Name: lista_espera_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lista_espera_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lista_espera_id_seq OWNER TO postgres;

--
-- TOC entry 3046 (class 0 OID 0)
-- Dependencies: 204
-- Name: lista_espera_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.lista_espera_id_seq OWNED BY public.lista_espera.id;


--
-- TOC entry 201 (class 1259 OID 74891)
-- Name: medicos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medicos (
    crm character varying(20) NOT NULL,
    nome character varying(120) NOT NULL,
    especialidade character varying(80) NOT NULL,
    plano_atendido character varying(80),
    senha character varying(100) NOT NULL
);


ALTER TABLE public.medicos OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 74884)
-- Name: pacientes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pacientes (
    cpf character varying(11) NOT NULL,
    nome character varying(120) NOT NULL,
    idade smallint NOT NULL,
    plano character varying(80),
    senha character varying(100) NOT NULL,
    CONSTRAINT ck_pacientes_cpf CHECK (((cpf)::text ~ '^[0-9]{11}$'::text)),
    CONSTRAINT ck_pacientes_idade CHECK (((idade >= 0) AND (idade <= 130)))
);


ALTER TABLE public.pacientes OWNER TO postgres;

--
-- TOC entry 2871 (class 2604 OID 74901)
-- Name: consultas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas ALTER COLUMN id SET DEFAULT nextval('public.consultas_id_seq'::regclass);


--
-- TOC entry 2873 (class 2604 OID 74920)
-- Name: lista_espera id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lista_espera ALTER COLUMN id SET DEFAULT nextval('public.lista_espera_id_seq'::regclass);


--
-- TOC entry 3036 (class 0 OID 74898)
-- Dependencies: 203
-- Data for Name: consultas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consultas (id, crm_medico, cpf_paciente, data_consulta, realizada) FROM stdin;
1	123456	52998224725	2026-11-13	f
4	458197	25271235812	2026-11-17	t
5	456124	11144477735	2026-11-10	f
6	654321	86113453987	2026-11-11	f
7	123456	86113453987	2026-11-12	t
8	456133	77777777777	2026-11-27	f
9	456143	25271235812	2026-11-24	f
\.


--
-- TOC entry 3039 (class 0 OID 74934)
-- Dependencies: 206
-- Data for Name: consultas_realizadas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consultas_realizadas (id_consulta, crm_medico, cpf_paciente, data_consulta, valor_consulta, sintomas, tratamento, exames, medicamentos, texto_avaliacao, estrelas) FROM stdin;
4	458197	25271235812	2026-11-17	0.00	Dor de cabeça, tosse, dor nas costas.	Sintomas de gripe.	Não é necessário.	Tomar coristina D por 02 semanas.		0
7	123456	86113453987	2026-11-12	125.22	Dores abdominais.	Infecção útero.	Ultrassom transvaginal.	Tomar bactrim 30mg por 07 dias.	Excelente.	5
\.


--
-- TOC entry 3038 (class 0 OID 74917)
-- Dependencies: 205
-- Data for Name: lista_espera; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lista_espera (id, crm_medico, cpf_paciente, data_consulta, criada_em) FROM stdin;
\.


--
-- TOC entry 3034 (class 0 OID 74891)
-- Dependencies: 201
-- Data for Name: medicos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medicos (crm, nome, especialidade, plano_atendido, senha) FROM stdin;
123456	Dra. Helena Ricci	Cardiologia	Unimed	$2a$10$wur4Gctz.WQBxFUHvvAlAe.zad5IK9NS6ys0ra7wPOB6sGchHt0cu
654321	Dr. Paulo Andrade	Clínica Geral	\N	$2a$10$wur4Gctz.WQBxFUHvvAlAe.zad5IK9NS6ys0ra7wPOB6sGchHt0cu
456124	Dr. Erika	Dentista	Odontoprev	$2a$10$1caaPlp7vl5904y5sx/m4OWiT1RG2XwvHaOwEfIPSuos71RCFznS6
458197	Dr. Alfredo Mangabeira	Cardiologista	Bradesco	$2a$10$MZe5wAgTlKbvgBKa.wXFluM0KYKc/HGykkeL.yrv8ejUNywdaX9P.
456133	Dr. Mariana Ximenez Santiago	Ginecologista	\N	$2a$10$K83WfQNi17SH6d0zGwZNUe0h/SJ4xFPGkE3MC7bxGwEMT6nJgGGdi
456143	Dr. Eduardo Miranda	Urologia	\N	$2a$10$5x0hlnuTpJZNES5WFFFLyeZ7oqktJFgo0BalZYdjnS6d7bsU36r0y
\.


--
-- TOC entry 3033 (class 0 OID 74884)
-- Dependencies: 200
-- Data for Name: pacientes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pacientes (cpf, nome, idade, plano, senha) FROM stdin;
52998224725	Maria Souza	34	Unimed	$2a$10$4/ATR3lWwqxQrpM2McqW2urKhj6nARQ3zwChb.goGnepNYXUyXY5y
11144477735	João Lima	52	\N	$2a$10$4/ATR3lWwqxQrpM2McqW2urKhj6nARQ3zwChb.goGnepNYXUyXY5y
86113453987	Virginia Lajes Fonseca	45	\N	$2a$10$CG9oECCYQFYNgIP0qv/aVesf9eHPBwGiYri7qAXBExdW3QanUs6fO
25271235812	EDILSON SALVADOR RICCI	56		$2a$10$WnsI67tMeZpulSHjYNBLD..GgJna7SOPj1AIV5EXoPSW4pBfvOZv.
77777777777	Carol	48		$2a$10$902pBDncYRUAN6XABYPnGOUMmJBa8.KWsF3xdHlAv8W.gq05pa6OG
\.


--
-- TOC entry 3047 (class 0 OID 0)
-- Dependencies: 202
-- Name: consultas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.consultas_id_seq', 9, true);


--
-- TOC entry 3048 (class 0 OID 0)
-- Dependencies: 204
-- Name: lista_espera_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.lista_espera_id_seq', 1, false);


--
-- TOC entry 2886 (class 2606 OID 74904)
-- Name: consultas consultas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_pkey PRIMARY KEY (id);


--
-- TOC entry 2893 (class 2606 OID 74946)
-- Name: consultas_realizadas consultas_realizadas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas_realizadas
    ADD CONSTRAINT consultas_realizadas_pkey PRIMARY KEY (id_consulta);


--
-- TOC entry 2891 (class 2606 OID 74923)
-- Name: lista_espera lista_espera_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lista_espera
    ADD CONSTRAINT lista_espera_pkey PRIMARY KEY (id);


--
-- TOC entry 2884 (class 2606 OID 74895)
-- Name: medicos medicos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicos
    ADD CONSTRAINT medicos_pkey PRIMARY KEY (crm);


--
-- TOC entry 2881 (class 2606 OID 74890)
-- Name: pacientes pacientes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pacientes
    ADD CONSTRAINT pacientes_pkey PRIMARY KEY (cpf);


--
-- TOC entry 2887 (class 1259 OID 74963)
-- Name: idx_consultas_medico_data; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consultas_medico_data ON public.consultas USING btree (crm_medico, data_consulta, realizada);


--
-- TOC entry 2888 (class 1259 OID 74964)
-- Name: idx_consultas_paciente; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consultas_paciente ON public.consultas USING btree (cpf_paciente, realizada);


--
-- TOC entry 2894 (class 1259 OID 74967)
-- Name: idx_consultas_realizadas_medico; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consultas_realizadas_medico ON public.consultas_realizadas USING btree (crm_medico);


--
-- TOC entry 2895 (class 1259 OID 74966)
-- Name: idx_consultas_realizadas_paciente; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consultas_realizadas_paciente ON public.consultas_realizadas USING btree (cpf_paciente, estrelas);


--
-- TOC entry 2889 (class 1259 OID 74965)
-- Name: idx_lista_espera_medico_data; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_lista_espera_medico_data ON public.lista_espera USING btree (crm_medico, data_consulta, id);


--
-- TOC entry 2882 (class 1259 OID 74962)
-- Name: idx_medicos_especialidade; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_medicos_especialidade ON public.medicos USING btree (especialidade);


--
-- TOC entry 2897 (class 2606 OID 74910)
-- Name: consultas consultas_cpf_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_cpf_paciente_fkey FOREIGN KEY (cpf_paciente) REFERENCES public.pacientes(cpf) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2896 (class 2606 OID 74905)
-- Name: consultas consultas_crm_medico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas
    ADD CONSTRAINT consultas_crm_medico_fkey FOREIGN KEY (crm_medico) REFERENCES public.medicos(crm) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2902 (class 2606 OID 74957)
-- Name: consultas_realizadas consultas_realizadas_cpf_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas_realizadas
    ADD CONSTRAINT consultas_realizadas_cpf_paciente_fkey FOREIGN KEY (cpf_paciente) REFERENCES public.pacientes(cpf) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2901 (class 2606 OID 74952)
-- Name: consultas_realizadas consultas_realizadas_crm_medico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas_realizadas
    ADD CONSTRAINT consultas_realizadas_crm_medico_fkey FOREIGN KEY (crm_medico) REFERENCES public.medicos(crm) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2900 (class 2606 OID 74947)
-- Name: consultas_realizadas consultas_realizadas_id_consulta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consultas_realizadas
    ADD CONSTRAINT consultas_realizadas_id_consulta_fkey FOREIGN KEY (id_consulta) REFERENCES public.consultas(id) ON DELETE RESTRICT;


--
-- TOC entry 2899 (class 2606 OID 74929)
-- Name: lista_espera lista_espera_cpf_paciente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lista_espera
    ADD CONSTRAINT lista_espera_cpf_paciente_fkey FOREIGN KEY (cpf_paciente) REFERENCES public.pacientes(cpf) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 2898 (class 2606 OID 74924)
-- Name: lista_espera lista_espera_crm_medico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lista_espera
    ADD CONSTRAINT lista_espera_crm_medico_fkey FOREIGN KEY (crm_medico) REFERENCES public.medicos(crm) ON UPDATE CASCADE ON DELETE RESTRICT;


-- Completed on 2026-11-10 16:12:42

--
-- PostgreSQL database dump complete
--

