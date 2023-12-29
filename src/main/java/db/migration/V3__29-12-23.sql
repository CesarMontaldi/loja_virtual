CREATE TABLE public.acesso_end_point (
	nome_end_point character varying,
	quantidade_acesso_end_point integer);
	
INSERT INTO public.acesso_end_point (
		nome_end_point, quantidade_acesso_end_point)
	VALUES ('consulta_nome_pf', 0);
	
ALTER TABLE acesso_end_point add constraint nome_end_point_unique UNIQUE (nome_end_point);