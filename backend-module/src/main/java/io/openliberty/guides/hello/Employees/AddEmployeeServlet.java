package io.openliberty.guides.hello.Employees;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy.Simple;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.DigestUtils;

import io.openliberty.guides.hello.model.Employee;
import io.openliberty.guides.hello.model.Person;

@WebServlet("/Employees/AddEmployee")
public class AddEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        reader.close();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(line);

    


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
        EntityManager em = emf.createEntityManager();
        try{
            //create and update new employee
            Employee employee = new Employee();
        
            //get the biggest Employeeid
            TypedQuery<Integer> query = em.createNamedQuery("Person.findMaxId", Integer.class);
            int newid = query.getSingleResult();

            //print root
            System.out.println("Root:");
            System.out.println(root);


            //change Date of Birth to Date
            Date dateOfBirth = Date.valueOf(root.path("DateOfBirth").asText());
            Date workShift = Date.valueOf(root.path("WorkShift").asText());

            //hash password with sha256
            String password = root.path("Password").asText();
            String hashedPassword = DigestUtils.sha256Hex(password);

            employee.setEmployeeId(newid+1);
            employee.setAssignment(root.path("Assignment").asText());
            employee.setWorkShift(workShift);
            employee.setPassword(hashedPassword);

            //create and update new person
            Person person = new Person();
            person.setPersonID(newid+1);
            person.setLastName(root.get("LastName").asText());
            person.setFirstName(root.get("FirstName").asText());
            person.setEmail(root.get("Email").asText());
            person.setPhonePreselection(root.get("PhonePreselection").asText());
            person.setPhoneNumber(root.get("PhoneNumber").asText());
            person.setDocumentNumber(root.get("DocumentNumber").asText());
            person.setDateOfBirth(dateOfBirth);

            //establish relation between employee and person
            employee.setPerson(person);

            em.getTransaction().begin();    //start transakcion
            em.persist(person);               //send data to db
            em.persist(employee);               //send data to db
            em.getTransaction().commit();   //end transaction;

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.getWriter().println(mapper.writeValueAsString(employee));
        }

            catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Employee was not created");
            }
        

    finally{
            em.close();
            emf.close();
        }

    
}
}
