package net.codejava;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SalesDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;//spring jdbc

	public List<Sale> list() {
		String sql = "SELECT * FROM SALES";

		List<Sale> listSale = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(Sale.class));

		return listSale;
	}
	
	public void save(Sale sale) {
		AtomicInteger ID_GENERATOR = new AtomicInteger(10);
		sale.setId(ID_GENERATOR.getAndIncrement());
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("sales").usingColumns("id","item", "quantity", "amount");
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(sale);
		
		insertActor.execute(param);		
	}
	
	public Sale get(int id) {
		String sql = "SELECT * FROM SALES WHERE id = ?";
		Object[] args = {id};
		Sale sale = jdbcTemplate.queryForObject(sql, args, BeanPropertyRowMapper.newInstance(Sale.class));
		return sale;
	}
	
	public void update(Sale sale) {
		String sql = "UPDATE SALES SET item=:item, quantity=:quantity, amount=:amount WHERE id=:id";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(sale);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		template.update(sql, param);		
	}
	
	public void delete(int id) {
		String sql = "DELETE FROM SALES WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
}
