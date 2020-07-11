package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	void insertSeller(Seller objSeller);

	void updateSeller(Seller objSeller);

	void deleteById(Integer id);

	Seller findById(Integer id);

	List<Seller> findAll();

	List<Seller> findByDepartment(Department department);
}
