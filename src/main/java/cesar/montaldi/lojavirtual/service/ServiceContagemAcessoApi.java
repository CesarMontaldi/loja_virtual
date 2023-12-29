package cesar.montaldi.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceContagemAcessoApi {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void atualizaAcessoEndPointPF() {
		jdbcTemplate.execute("begin; update acesso_end_point set quantidade_acesso_end_point = quantidade_acesso_end_point + 1 where nome_end_point = 'consulta_nome_pf'; commit;");
	}

}
