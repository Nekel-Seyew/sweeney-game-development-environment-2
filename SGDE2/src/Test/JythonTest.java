/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

/**
 *
 * @author kdsweenx
 */
public class JythonTest {
    private PyObject jyEmployeeClass;
    public JythonTest(){
        PythonInterpreter interpreter = new PythonInterpreter();
        System.out.println("Interpreter");
        interpreter.exec("from Test import Employee");
        System.out.println("Interpreter.get");
        jyEmployeeClass = interpreter.get("Employee");
        System.out.println("Gotten");
    }
    
    public JythonEmployeeType create(String first, String last, String id){
        PyObject employeeObj = jyEmployeeClass.__call__(new PyString(first), new PyString(last), new PyString(id));
        return (JythonEmployeeType)employeeObj.__tojava__(JythonEmployeeType.class);
    }
    
    public static void main(String[] args){
        JythonEmployeeType test=new JythonTest().create("John", "smith", "1234");
        System.out.println(test.getEmployeeFirst());
        System.out.println(test.getEmployeeLast());
        System.out.println(test.getEmployeeId());
    }
}
