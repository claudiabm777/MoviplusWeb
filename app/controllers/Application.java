package controllers;

import models.Moviplus;
import models.SimulacionVecinos;
import play.*;
import play.data.DynamicForm;
import play.mvc.*;

import views.html.*;

import java.io.File;

import static play.data.Form.form;

public class Application extends Controller {

    public Result index() {
        return ok(index.render());
    }

    public  Result multiple(){
        try {
            Moviplus mundo = new Moviplus();
            DynamicForm bindedForm = form().bindFromRequest();
            System.out.println(bindedForm.get("min") + "  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            String min = bindedForm.get("min").trim();
            String max = bindedForm.get("max").trim();
            String num = bindedForm.get("num").trim();
            double ts = (double)Integer.parseInt(min);
            double ti = (double)Integer.parseInt(max);
            double n = (double)Integer.parseInt(num);
            File file=mundo.simulacionMultiple(ts, ti, n);
            return ok(new File("/tmp/resultados.xls"));
        }catch (Exception ee){
            return badRequest();
        }
    }

    public  Result simple(){
        Moviplus mundo=new Moviplus();
        DynamicForm bindedForm = form().bindFromRequest();
        System.out.println(bindedForm.get("sen") + "  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String sen = bindedForm.get("sen").trim();
        double t=(double)Integer.parseInt(sen);



        File file=mundo.simulacionOptimizacion(t, 0);
        return ok(new File("/tmp/resultados.xls"));
    }

    public Result vecinosCercanos(){
        return ok(vecinos.render());
    }
}
