package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertSeller(Seller objSeller) {
		PreparedStatement preparedStat = null;
		try {
			preparedStat = conn.prepareStatement("insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ "values " + "(?, ? , ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			preparedStat.setString(1, objSeller.getName());
			preparedStat.setString(2, objSeller.getEmail());
			preparedStat.setDate(3, new java.sql.Date(objSeller.getBirthDate().getTime()));
			preparedStat.setDouble(4, objSeller.getBaseSalary());
			preparedStat.setInt(5, objSeller.getDepartment().getId());

			int rowsAffected = preparedStat.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet resultAffectedRows = preparedStat.getGeneratedKeys();

				if (resultAffectedRows.next()) {
					int idInsert = resultAffectedRows.getInt(1);
					objSeller.setId(idInsert);
				}
			} else {
				throw new DbException("Insert não concluido! Erro Inesperado.");
			}
		} catch (SQLException e) {
			throw new DbException("Erro Inesperado: " + e.getMessage());
		} finally {
			DB.closeStatement(preparedStat);
		}
	}

	@Override
	public void updateSeller(Seller objSeller) {
		PreparedStatement preparedStat = null;
		try {
			preparedStat = conn.prepareStatement("update seller "
					+ "set Name= ?, Email= ?, BirthDate= ?, BaseSalary= ?, DepartmentId= ?" + " where Id= ?");
			preparedStat.setString(1, objSeller.getName());
			preparedStat.setString(2, objSeller.getEmail());
			preparedStat.setDate(3, new java.sql.Date(objSeller.getBirthDate().getTime()));
			preparedStat.setDouble(4, objSeller.getBaseSalary());
			preparedStat.setInt(5, objSeller.getDepartment().getId());
			preparedStat.setInt(6, objSeller.getId());
			preparedStat.executeUpdate();

		} catch (SQLException e) {
			throw new DbException("Erro Inesperado: " + e.getMessage());
		} finally {
			DB.closeStatement(preparedStat);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement prepStat = null;
		try {
			prepStat = conn.prepareStatement("delete from seller where id = ?");
			prepStat.setInt(1, id);
			prepStat.execute();
		} catch (SQLException e) {
			throw new DbException("Erro ao Deletar: " + e.getMessage());
		} finally {
			DB.closeStatement(prepStat);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"" + "select seller.*,department.Name as DepName\r\n" + "from seller inner join department \r\n"
							+ "on seller.DepartmentId = Department.Id\r\n" + "where seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department departament = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, departament);

				return seller;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException("Erro ao Consultar: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department departament) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(departament);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department departament = new Department();
		departament.setId(rs.getInt("DepartmentId"));
		departament.setName(rs.getString("DepName"));
		return departament;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select seller.*,department.Name as DepName " + " from seller inner join department "
							+ "on seller.DepartmentId = department.Id " + " order by Name");
			rs = st.executeQuery();
			List<Seller> sellerList = new ArrayList<Seller>();
			Map<Integer, Department> mapControll = new HashMap<>();

			while (rs.next()) {
				Department departament = mapControll.get(rs.getInt("departmentId"));
				if (departament == null) {
					departament = instantiateDepartment(rs);
					mapControll.put(rs.getInt("departmentId"), departament);
				}

				sellerList.add(instantiateSeller(rs, departament));
			}
			return sellerList;
		} catch (SQLException e) {
			throw new DbException("Erro ao Consultar: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select seller.*,department.Name as DepName " + " from seller inner join department "
							+ "on seller.DepartmentId = department.Id " + "where DepartmentId = ? " + " order by Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			List<Seller> sellerList = new ArrayList<Seller>();
			Map<Integer, Department> mapControll = new HashMap<>();

			while (rs.next()) {
				Department departament = mapControll.get(rs.getInt("departmentId"));
				if (departament == null) {
					departament = instantiateDepartment(rs);
					mapControll.put(rs.getInt("departmentId"), department);
				}

				sellerList.add(instantiateSeller(rs, departament));
			}
			return sellerList;
		} catch (SQLException e) {
			throw new DbException("Erro ao Consultar: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
}
