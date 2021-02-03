/**
 * Author_code: B180910040
 * Author_name: I.Sainjargal
 * Created_Date&Time: 2021/1/2 23:06
 * Last_Modified_Date&Time: 2021/1/3 03:43
 * Lab: 2-5
 */
package com.CS314.Lab2.Repository;

import com.CS314.Lab2.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
