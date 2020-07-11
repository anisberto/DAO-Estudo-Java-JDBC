package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertDepartment(Department objDepartment) {
		PreparedStatement prepInsert = null;
		try {
			prepInsert = conn.prepareStatement("insert into department (Name)" + "values " + "(?)",
					Statement.RETURN_GENERATED_KEYS);
			prepInsert.setString(1, objDepartment.getName());
			int rowsAffected = prepInsert.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet resultAffectedRows = prepInsert.getGeneratedKeys();
				if (resultAffectedRows.next()) {
					int idInsert = resultAffectedRows.getInt(1);
					objDepartment.setId(idInsert);
				}
			} else {
				throw new DbException("Insert não concluido! Erro Inesperado.");
			}
		} catch (SQLException e) {
			throw new DbException("Erro ao incluir" + e.getMessage());
		} finally {
			DB.closeStatement(prepInsert);
		}
	}

	@Override
	public void updateDepartment(Department objDepartment) {
		PreparedStatement prepInsert = null;
		try {
			prepInsert = conn.prepareStatement("update department " + "set Name= ? " + " where Id= ?");
			prepInsert.setString(1, objDepartment.getName());
			prepInsert.setInt(2, objDepartment.getId());
			prepInsert.executeUpdate();
		} catch (SQLException e) {
			throw new DbException("Erro update" + e.getMessage());
		} finally {
			DB.closeStatement(prepInsert);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement prepStat = null;
		try {
			prepStat = conn.prepareStatement("delete from department where id = ?");
			prepStat.setInt(1, id);
			prepStat.execute();
		} catch (SQLException e) {
			throw new DbException("Erro ao Deletar: " + e.getMessage());
		} finally {
			DB.closeStatement(prepStat);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement preparedFindByid = null;
		ResultSet resultFindById = null;
		try {
			preparedFindByid = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
			preparedFindByid.setInt(1, id);
			resultFindById = preparedFindByid.executeQuery();
			if (resultFindById.next()) {
				Department obj = new Department();
				obj.setId(resultFindById.getInt("Id"));
				obj.setName(resultFindById.getString("Name"));
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedFindByid);
			DB.closeResultSet(resultFindById);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement preparedFindAll = null;
		ResultSet resultFindAll = null;
		try {
			preparedFindAll = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
			resultFindAll = preparedFindAll.executeQuery();
			List<Department> list = new ArrayList<>();
			while (resultFindAll.next()) {
				Department obj = new Department();
				obj.setId(resultFindAll.getInt("Id"));
				obj.setName(resultFindAll.getString("Name"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedFindAll);
			DB.closeResultSet(resultFindAll);
		}
	}

}
