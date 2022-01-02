package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAmin = entityManager.find(Role.class,1);
		User userIshworK = new User("khatriiishwor@gmail.com","ipLancks1","Ishwor","Khatri");
		userIshworK.addRole(roleAmin);
		
		User savedUser = repo.save(userIshworK);
		
		assertThat(savedUser.getId()).isGreaterThan(0);	
	}
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userSujita= new User("sujita@gmail.com","sujita2020","Sujita","Khadka");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userSujita.addRole(roleEditor);
		userSujita.addRole(roleAssistant);
		
		User savedUser = repo.save(userSujita);
		
		assertThat(savedUser.getId()).isGreaterThan(0);	
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		User userIshwor = repo.findById(1).get();
		System.out.println(userIshwor);
		assertThat(userIshwor).isNotNull();
		
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userIshwor = repo.findById(1).get();
		userIshwor.setEnabled(true);
		userIshwor.setPassword("ishwor2020");
		
		repo.save(userIshwor);
		
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userSujita = repo.findById(2).get();
		Role roleEditior = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userSujita.getRoles().remove(roleEditior);
		userSujita.addRole(roleSalesperson);
		
		repo.save(userSujita);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	@Test
	public void testGetUserByEmail() {
		String email = "sujita@gmail.com";
		User user =repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id =1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
		
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword,pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}

	

}
