package com.cs673olsum24.promanager.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cs673olsum24.promanager.entity.Projects;


@Repository
@Transactional
@SuppressWarnings("unchecked")
public class ProjectDAO {
	@Autowired
	private EntityManager entityManager;	 
	public ProjectDAO(NamedParameterJdbcTemplate template) {  
		this.template = template;  
	}


	NamedParameterJdbcTemplate template;

	/**
	 * Retrieves all project records from the database.
	 *
	 * This method executes a native SQL query to fetch all columns of the project records
	 * from the `project_ci` table. The retrieved columns include project ID, project name,
	 * user ID, task ID, description, created date, updated date, status, and type.
	 *
	 * @return A list of Object arrays, where each array represents a row from the `project_ci`
	 *         table. Each Object array contains the following columns:
	 *         - projectid (Integer)
	 *         - projectname (String)
	 *         - userid (Integer)
	 *         - taskid (Integer)
	 *         - description (String)
	 *         - created_on (Timestamp)
	 *         - updated_on (Timestamp)
	 *         - status (String)
	 *         - type (String)
	 *         
	 *         If an exception occurs during query execution, an empty list is returned.
	 *
	 * @throws IllegalArgumentException If the query string is invalid.
	 * @throws PersistenceException If there is a problem executing the query.
	 */
	public List<Object[]> findAllProjects() {
		try {
			String sql = "SELECT p.projectid, p.projectname , p.userid, p.taskid, p.description, p.created_on, p.updated_on, p.status, p.type FROM project_ci p";
			Query query = entityManager.createNativeQuery(sql);
			List<Object[]> results = query.getResultList();
			return query.getResultList();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public void addProjects(Projects p) {

		final String sql = "insert into project_ci(projectid,projectname,userid,taskid,description,created_on,updated_on,status,type) "
				+ "values(:projectid,:projectname,:userid,:taskid,:description,:created_on,:updated_on,:status,:type)";	

		Map<String,Object> map=new HashMap<>();  
		map.put("projectid", p.getProjectid());
		map.put("projectname", p.getProjectname());
		map.put("userid", p.getUserid());
		map.put("taskid", p.getTaskid());
		map.put("description", p.getDescription());
		map.put("created_on", p.getCreated_on());
		map.put("updated_on", p.getUpdated_on());
		map.put("status", p.getStatus());
		map.put("type", p.getType());	

		template.execute(sql,map,new PreparedStatementCallback<Object>() {  
			@Override  
			public Object doInPreparedStatement(PreparedStatement ps)  
					throws SQLException {  
				return ps.executeUpdate(); 
			}  
		});
	}


	public String deleteProject(String id) {		    	

		final String sql1 ="delete from project_ci WHERE projectid= :id";
		Map<String,Object> map1=new HashMap<>(); 

		map1.put("id",id);

		template.execute(sql1,map1,new PreparedStatementCallback<Object>() {  
			@Override  
			public Object doInPreparedStatement(PreparedStatement ps)  
					throws SQLException {  
				return ps.executeUpdate();  
			}  
		});
		return "success";
	}



	public void safeDeleteProject(Projects p) {

		final String sql1 ="UPDATE project_ci SET status= inactive WHERE projectid= :projectid";

		Map<String,Object> map=new HashMap<>();

		map.put("projectid", p.getProjectid());	 	
		map.put("status", p.getStatus());

		template.execute(sql1,map,new PreparedStatementCallback<Object>() {  
			@Override  
			public Object doInPreparedStatement(PreparedStatement ps)  
					throws SQLException {  
				return ps.executeUpdate();  
			}  
		});
	}



	public void editProject(Projects p) {

		final String sql1 ="UPDATE project_ci SET projectname=:projectname,userid=:userid,taskid=:taskid,description=:description ,updated_on= :updated_on, status=:status,type=:type WHERE projectid= :projectid";

		Map<String,Object> map=new HashMap<>();  
		map.put("projectid", p.getProjectid());
		map.put("projectname", p.getProjectname());
		map.put("userid", p.getUserid());
		map.put("taskid", p.getTaskid());
		map.put("description", p.getDescription());
		map.put("updated_on", p.getUpdated_on());
		map.put("status", p.getStatus());
		map.put("type", p.getType());




		template.execute(sql1,map,new PreparedStatementCallback<Object>() {  
			@Override  
			public Object doInPreparedStatement(PreparedStatement ps)  
					throws SQLException {  
				return ps.executeUpdate();  
			}  
		});
	}

}
