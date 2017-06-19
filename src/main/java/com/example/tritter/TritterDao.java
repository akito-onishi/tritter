
package com.example.tritter;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TritterDao {
    
    @Autowired
    private JdbcTemplate jdbc;

    public List<Person> findAll() {
        return jdbc.query("SELECT * FROM PERSON",new BeanPropertyRowMapper<>(Person.class));
    }

    public List<Map<String, Object>> findByAge(int id){
        return jdbc.queryForList("SELECT * FROM PERSON ID=?",id);
    }
}
