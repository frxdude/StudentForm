/**
 * Author_code: B180910040
 * Author_name: I.Sainjargal
 * Created_Date&Time: 2021/1/2 23:06
 * Last_Modified_Date&Time: 2021/1/3 03:43
 * Lab: 2-5
 */
package com.CS314.Lab2.Controller;

import com.CS314.Lab2.Model.Student;
import com.CS314.Lab2.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {
    @Autowired
    private StudentRepository stuRepo;
    //Ashiglagdah togtmol utguud
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/lab2";
    private static final String USERNAME = "CS314";
    private static final String PASSWORD = "B180910040";

    //huuramchaar file uusgen projectiin root pathiig avch baina
    File file = new File("");
    String absolutePath = file.getAbsolutePath();

    private final String FILE_PATH = absolutePath + "/temp";
    private final String LOG_PATH = absolutePath + "/log";

    //DB service asaalttai esehiig shalgah variable
    private boolean connectionCheck = false;

    //Log bichih date format
    DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");

    //Paths
    String logPath;
    String filePath = FILE_PATH + "/data.txt";

    /**
     * Description:
     *  Student List butsaana
     *
     * Method: Get
     * Header: No
     * Body: No
     *
     * @return :
     * 200 - OK - amjilttai tohioldold
     * 400 - BAD_REQUEST - ymar negen aldaa garsan uyd
     */
    @GetMapping("/student")
    public ResponseEntity<?> addStudent() {
        try{
            return new ResponseEntity<>(stuRepo.findAll(),HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Description:
     *  Student add hiih uyd checkService function-iig duudaj
     * ajilluulsnaar connection-ii utgiig shinechlen tuhain utgaas
     * hamaarsan uildliig guitsetgene
     * <p>
     * Method: Post
     * Header: No
     * Body:
     * {
     * "major": "",
     * "year": "",
     * "name": "",
     * "code": "",
     * "lab": ""
     * }
     *
     * @param student hadgalah Student-iin medeelel
     * @return :
     * 200 - OK - amjilttai tohioldold
     * 400 - BAD_REQUEST - ymar negen aldaa garsan uyd
     */
    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        LocalDateTime now = LocalDateTime.now();
        try {
            checkService();
            if (connectionCheck) {
                stuRepo.save(student);
                System.out.println("Data saved to Database");
            } else {
                logPath = LOG_PATH + "/" + date.format(now) + ".txt";
                File logFile = new File(logPath);

                // true - file umnu ni baigaagui uyd
                // false -  tuhain file ali hediin orshin baigaag iltgene
                logFile.createNewFile();
                String tempLogData = time.format(now);
                writeToFile(logPath, tempLogData);
                System.out.println("File created: " + logFile.getName());
                /**
                 * | temdgeer datag concat hiij baina.
                 * ingesneer file-iig unshij avahdaa split function-oor
                 * datag salgaj avah bolomjtoi. Harin ene baidlaaraa DB-d
                 * hadgalagdah yum.
                 */
                String data = student.getCode() + "|" + student.getLab() + "|"
                        + student.getName() + "|" + student.getMajor() + "|" + student.getYear();
                writeToFile(filePath, data);
                System.out.println("Data saved to File");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(stuRepo.findAll(),HttpStatus.OK);
    }

    /**
     * Description:
     *  MySQL ruu holbolt hiij uunees catch deer
     * SQLException barij avsnaar tuhain server asaalttai
     * uguig barij avch connectionCheck geh boolean utgiig
     * toggle hiij baina.
     * <p>
     * Method: Get
     * Header: No
     * Body: NO
     *
     * @return :
     * 200 - OK - amjilttai tohioldold
     *     - response.data = true
     * 200 - OK - amjiltgui tohioldold
     *     - response.data = false
     */
    @GetMapping("/checkConnection")
    public ResponseEntity<?> checkService() throws IOException {
        //Initialize
        Connection connection = null;
        File file = new File(filePath);
        Scanner myReader = new Scanner(file);
        //SQLException deerees serveriin status barij avah try catch
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connectionCheck = true;
            System.out.println("CONNECTED");
            // Ergeed connected boloh uyd file-aas datagaa unshij DB ruu hiine
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] datas = data.split("\\|"); //datagaa salgaj baina
                Student tempStu = new Student();
                tempStu.setCode(datas[0]); //code
                tempStu.setLab(datas[1]); //lab
                tempStu.setName(datas[2]); //name
                tempStu.setMajor(datas[3]); //major
                tempStu.setYear(datas[4]); //year
                stuRepo.save(tempStu);
                System.out.println(data);
            }
            myReader.close();

            //File dotorhiig tseverlehiin tuld file ruu hooson String hevlej baina.
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (SQLException e) {
            connectionCheck = false;
            System.out.println("NOT CONNECTED");
            return new ResponseEntity<>(false,HttpStatus.OK);
        }
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    /**
     * Description:
     *  File ruu bichih function
     *
     * @param path bichih file-iin zam
     * @param data bichih ugugdul
     */
    private void writeToFile(String path, String data) {
        try {
            FileWriter myWriter = new FileWriter(path, true);         // bichih file-iig zaaj baina.
            myWriter.write(data + "\n");                   // data ni dogol muruur tusgaarlagdah bolno.
            myWriter.close();                                  // orolt garaltiin suvgiig haaj baina.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

