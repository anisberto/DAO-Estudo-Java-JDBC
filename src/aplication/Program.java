package aplication;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		SellerDao sellerDao = DaoFactory.createSellerDao();
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

		while (true) {
			System.out.println("você quer tratade de 0 - DEPARTMENT || 1 - SELLER");
			int dec = input.nextInt();

			switch (dec) {
			case 1:
				System.out.print("Informe o Id do Vendedor: ");
				int id = input.nextInt();

				System.out.println("\n==== Find by Id ====\n");
				Seller seller = sellerDao.findById(id);
				System.out.println(seller);

				System.out.println("\n====== Find by Department ======\n");
				List<Seller> listSeller = sellerDao.findByDepartment(sellerDao.findById(id).getDepartment());
				for (Seller obj : listSeller) {
					System.out.println(obj);
				}

				System.out.println("\n====== Find All ======\n");
				List<Seller> listSellerAll = sellerDao.findAll();
				for (Seller sellerAllList : listSellerAll) {
					System.out.println(sellerAllList);
				}

				System.out.println("\n====== Inserted New Seller ======\n");
				Seller newSeller = new Seller(null, "Aline Maria", "Aline@gmail.com", new Date(), 2000.25,
						new Department(2, null));
				sellerDao.insertSeller(newSeller);
				System.out.println("Inserted Seller id : " + newSeller.getId() + ", Name: " + newSeller.getName());

				System.out.println("\n====== Update Old Seller ======\n");
				seller = sellerDao.findById(id);
				seller.setName("Martha Waine");
				sellerDao.updateSeller(seller);
				System.out.println("Update Seller id : " + seller.getId() + ", Name: " + seller.getName());

				System.out.println("\n====== Delete Seller By Id ======\n");
				System.out.println("Informe um id para deleção: ");
				Integer idDelete = input.nextInt();
				sellerDao.deleteById(idDelete);
				System.out.println("Seller deletado: " + idDelete);
				break;

			case 0:
				input.nextLine();
				System.out.println(
						"Quanoto ao Department Você Deseja\n0 - INSERIR || 1 - UPDATE|| 2 - DELETAR || 3 - CONSULTAR TODOS || 4 - CONSULTAR POR ID");
				int departmentSelect = input.nextInt();

				switch (departmentSelect) {
				case 0:
					System.out.println("\n === Insert Department === ");
					System.out.println("Informe o nome do Department");
					String departmentName = input.nextLine();
					Department department = new Department(null, departmentName);
					departmentDao.insertDepartment(department);
					System.out.println(
							"Department inserted - Id: " + department.getId() + ", Name: " + department.getName());
					break;
				case 1:
					System.out.println("\n === Update Department === ");
					System.out.println("informe o id para atualizar: ");
					int id1 = input.nextInt();
					input.nextLine();
					Department deparmentUpdate = departmentDao.findById(id1);
					System.out.println("Informe o novo nome do Department");
					String newName = input.nextLine();
					deparmentUpdate.setName(newName);
					departmentDao.updateDepartment(deparmentUpdate);
					System.out.println("Update Department id : " + deparmentUpdate.getId() + ", Name: "
							+ deparmentUpdate.getName());
					break;
				case 2:
					input.nextLine();
					System.out.println("\n === Delete Department === ");
					System.out.println("Informe o Id do Department");
					Integer idDepDelete = input.nextInt();
					departmentDao.deleteById(idDepDelete);
					System.out.println("Department Deletado! ");
					break;
				case 3:
					System.out.println("\n === Find All Department === ");
					List<Department> departmentList = departmentDao.findAll();
					System.out.println("Departments:. ");
					for (Department depList : departmentList) {
						System.out.println("Id: " + depList.getId() + ", Name: " + depList.getName());
					}
					break;
				case 4:
					input.nextLine();
					System.out.println("\n === Insert Department === ");
					System.out.println("Informe o Id do Department");
					Integer idDep = input.nextInt();
					System.out.println("Id: " + departmentDao.findById(idDep).getId() + ", Name: "
							+ departmentDao.findById(idDep).getName());
					break;
				default:
					break;
				}

			}
		}
	}
}
