package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insertDepartment(Department objDepartment);

	void updateDepartment(Department objDepartment);

	void deleteById(Integer id);

	Department findById(Integer id);

	List<Department> findAll();
}
