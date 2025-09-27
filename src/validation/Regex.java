/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

/**
 *
 * @author mgssr
 */
public class Regex {

    public enum Validation {
        EMAIL() {
            @Override
            public String validate() {
                return "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            }
        },
        USERNAME() {
            @Override
            public String validate() {
                return "^[a-zA-Z0-9](?!.*[_.]{2})[a-zA-Z0-9._]{1,18}[a-zA-Z0-9]$";
            }
        },
        MOBILE() {
            @Override
            public String validate() {
                return "^(0{1})(7{1})([0|1|2|4|5|6|7|8]{1})([0-9]{7})";
            }
        },
        NIC() {
            @Override
            public String validate() {
                return "^(?:\\d{9}[Vv]|\\d{12})$";
            }
        },
        PASSWORD() {
            @Override
            public String validate() {
                return "^.*(?=.{4,8})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
            }
        };

        public String validate() {
            return "";
        }
    }
}
