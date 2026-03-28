package com.itheima.studentsystem;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ArrayList<User> list = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("欢迎来到学生管理系统");
            System.out.println("请选择操作：1登录、2注册、3忘记密码");
            String choose = sc.next();

            switch (choose) {
                case "1" -> login(list);
                case "2" -> register(list);
                case "3" -> forgetPassword(list);
                case "4" -> {
                    System.out.println("谢谢使用，再见");
                    System.exit(0);
                }
                default -> System.out.println("没有这个选项");
            }
        }

    }

    private static void login(ArrayList<User> list) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.println("请输入用户名：");
            String username = sc.next();

            boolean flag = contains(list, username);
            if (!flag) {
                System.out.println("用户名：" + username + "未注册，请先注册后再登录");
                return;
            }

            System.out.println("请输入密码：");
            String password = sc.next();

            while (true) {
                String rightCode = getCode();
                System.out.println("当前验证码为：" + rightCode);
                System.out.println("请输入验证码：");
                String code = sc.next();
                if (code.equalsIgnoreCase(rightCode)) {
                    System.out.println("验证码正确");
                    break;
                } else {
                    System.out.println("验证码有误，请重新输入");
                    continue;
                }
            }

            User userInfo = new User(username, password, null, null);

            boolean result = checkUserInfo(list, userInfo);
            if (result) {
                System.out.println("用户名和密码正确，欢迎使用学生管理系统");
                StudentSystem ss = new StudentSystem();
                ss.startStudentSystem();
                break;
            } else {
                System.out.println("登录失败，用户名或密码有误");
                if (i == 2) {
                    System.out.println("当前用户名" + username + "已锁定，请联系黑马程序员");
                    return;
                } else {
                    System.out.println("用户名或密码错误，您还有" + (2 - i) + "次机会");
                }
            }
        }
    }

    public static void register(ArrayList<User> list) {

        //键盘录入用户名
        Scanner sc = new Scanner(System.in);
        String username;
        while (true) {
            System.out.println("请输入用户名：");
            username = sc.next();

            boolean flag1 = checkUsername(username);
            if (!flag1) {
                //不符合要求，请重新输入
                System.out.println("用户名：" + username + "不符合要求，请重新输入");
                continue;
            }

            //继续验证唯一性
            boolean flag2 = contains(list, username);
            if (flag2) {
                //用户名已存在，不能注册
                System.out.println("用户名：" + username + "已存在，不能注册该用户名");
            } else {
                //用户名不存在
                System.out.println("用户名：" + username + "已生效");
                break;
            }
        }

        //键盘录入密码
        String password;
        while (true) {
            System.out.println("请输入要注册的密码：");
            password = sc.next();

            System.out.println("请再输入一次密码进行确认：");
            String againPassword = sc.next();

            //判断一致性
            if (!(password.equals(againPassword))) {
                //不一致，提示重新确认密码
                System.out.println("两次输入密码不一致，请重新输入");
            } else {
                //一致
                System.out.println("两次输入密码一致，请继续录入其他信息");
                break;
            }
        }

        //键盘录入身份证号码
        String idCardNumber;
        while (true) {
            System.out.println("请输入您本人的身份证号码：");
            idCardNumber = sc.next();

            boolean flag = checkIdCardNumber(idCardNumber);
            if (!flag) {
                //身份证号码有误，需重新输入
                System.out.println("身份证号码有误，请重新输入");
                continue;
            } else {
                //身份证号码符合要求，继续录入其他信息
                System.out.println("身份证号码有效，请继续录入其他信息");
                break;
            }
        }

        //键盘录入手机号码
        String phoneNumber;
        while (true) {
            System.out.println("请输入您的手机号码：");
            phoneNumber = sc.next();
            boolean flag = checkPhoneNumber(phoneNumber);
            if (flag) {
                //符合要求
                System.out.println("手机号码有效");
                break;
            } else {
                //不符合要求，需重新输入
                System.out.println("手机号码有误，请重新输入");
                continue;
            }
        }

        //用户名、密码、身份证号码、手机号码都放在用户对象中
        User user = new User(username, password, idCardNumber, phoneNumber);

        //最后将用户对象放入集合中
        list.add(user);
        System.out.println("注册成功");

        //遍历集合
        printList(list);

    }

    public static void forgetPassword(ArrayList<User> list) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.next();

        boolean flag = contains(list, username);
        if (!flag) {
            System.out.println("当前用户名未注册，请先注册");
            return;
        }

        System.out.println("请输入您的身份证号码：");
        String idCardNumber = sc.next();

        System.out.println("请输入您的手机号码：");
        String phoneNumber = sc.next();

        int index = fingIndex(list, username);
        User user = list.get(index);
        if (!((user.getIdCardNumber().equalsIgnoreCase(idCardNumber)) && (user.getPhoneNumber().equals(phoneNumber)))){
            System.out.println("身份证号码或者手机号码错误，无法修改");
            return;
        }

        String password;
        while (true) {
            System.out.println("请输入新的密码:");
            password = sc.next();

            System.out.println("请再次输入新的密码:");
            String againPassword = sc.next();

            if (password.equals(againPassword)){
                System.out.println("两次密码一致");
                break;
            } else {
                System.out.println("两次密码不一致，请重新输入");
                continue;
            }
        }

        user.setPassword(password);
        System.out.println("密码修改成功");
    }

    private static int fingIndex(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (user.getUsername().equals(username)){
                return i;
            }
        }
        return -1;
    }

    private static boolean checkUserInfo(ArrayList<User> list, User userInfo) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (userInfo.getUsername().equals(user.getUsername()) && userInfo.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private static void printList(ArrayList<User> list) {
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            System.out.println("用户名：" + u.getUsername() + ", " + "密码：" + u.getPassword() + ", "
                    + "身份证号码：" + u.getIdCardNumber() + ", " + "手机号码：" + u.getPhoneNumber());
        }
    }

    private static boolean checkPhoneNumber(String phoneNumber) {
        //长度为11位
        int len = phoneNumber.length();
        if (len != 11) {
            return false;
        }

        //不能以0为开头
        if (phoneNumber.startsWith("0")) {
            return false;
        }

        //必须都是数字
        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkIdCardNumber(String idCardNumber) {
        //长度为18位
        int len = idCardNumber.length();
        if (len != 18) {
            return false;
        }

        //不能以0为开头
        /*char c1 = idCardNumber.charAt(0);
        if (c1 == '0'){
            return false;
        }*/

        if (idCardNumber.startsWith("0")) {
            return false;
        }

        //前17位必须是数字
        for (int i = 0; i < idCardNumber.length() - 1; i++) {
            char c = idCardNumber.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }

        //最后一位可以是数字，也可以是大写X或者小写x
        char endchar = idCardNumber.charAt(idCardNumber.length() - 1);
        if (!((endchar >= '0' && endchar <= '9') || (endchar == 'X') || (endchar == 'x'))) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean contains(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            String rightUsername = user.getUsername();
            if (rightUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkUsername(String username) {
        //判断长度
        int len = username.length();
        if (len < 3 || len > 15) {
            return false;
        }

        //判断内容：1.只能是字母加数字的组合
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))) {
                return false;
            }
        }

        //2.不能是纯数字
        int count = 0;
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                count++;
                break;
            }
        }
        return count > 0;
    }

    /*  private static String getCode() {
          //方法一
          String code = "";
          char[] arr = new char[52];
          Random r = new Random();
          char[] codeArr = new char[5];
          for (int i = 0; i < 4; i++) {
              for (int j = 0; j < arr.length; j++) {
                  if (j <= 25) {
                      arr[j] = (char) (j + 97);
                  } else {
                      arr[j] = (char) (j + 65 - 26);
                  }
                  int randomIndex = r.nextInt(arr.length);
                  codeArr[i] = arr[randomIndex];
              }
          }
          int randomNumber = r.nextInt(10);
          codeArr[codeArr.length - 1] = (char) (randomNumber + 48);

          for (int i = 0; i < codeArr.length; i++) {
              int randomIndex = r.nextInt(codeArr.length);
              char temp = codeArr[i];
              codeArr[i] = codeArr[randomIndex];
              codeArr[randomIndex] = temp;
          }

          for (int i = 0; i < codeArr.length; i++) {
              code = code + codeArr[i];
          }
          return code;
      }*/

    //方法二
    private static String getCode() {
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char) ('a' + i));
            list.add((char) ('A' + i));
        }
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = r.nextInt(list.size());
            sb.append(list.get(index));
        }

        int number = r.nextInt(10);
        sb.append(number);

        char[] arr = sb.toString().toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int randomIndex = r.nextInt(arr.length);
            char temp = arr[randomIndex];
            arr[randomIndex] = arr[arr.length - 1];
            arr[arr.length - 1] = temp;
        }

        return new String(arr);
    }
}

