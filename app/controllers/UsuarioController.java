package controllers;


import jxl.write.WriteException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by scvalencia606 on 8/10/15.
 */
public class UsuarioController extends Controller {

    public Result read() throws IOException {

        try {

           // WriteExcel test = new WriteExcel();
           // test.setOutputFile("/tmp/ughhh.xls");
            try {
                //   test.write();
            } catch (Exception e) {
                e.printStackTrace();
            }

                        String content = "This is the content to write into file";

            File file = new File("/tmp/resultados.xls");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");
            return ok(new File("/tmp/resultados.xls"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ok("asfds");

    }
}
