package ru.bidone.hint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ru.bidone.hint.R;

public class CalcFragment extends Fragment {

    View rootView;
    private TextView tvAnswer,  tvExpr, tvClean, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv0, tvPlus, tvMinus, tvUmnoj, tvDeli, tvRavno, tvPM, tvComma, tvProc;
    private Button btnDel;

    public CalcFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_calc, container, false);
            rootView.findViewById(R.id.ivBack).setVisibility(View.GONE);
            ((TextView) rootView.findViewById(R.id.tvTitle)).setText(R.string.tab_calc);
            init();
        }
        return rootView;
    }

    private void init() {
        tvAnswer = rootView.findViewById(R.id.tvAnswer); // переменной tvAnswer (текстовое поле) присваиваем пекстовое поле из Пользовательского Интерфейса с id = tvAnswer
        tvExpr = rootView.findViewById(R.id.tvExpr);
        tvClean = rootView.findViewById(R.id.tvClean);
        btnDel = rootView.findViewById(R.id.btnDel);
        tv0 = rootView.findViewById(R.id.tv0);
        tv1 = rootView.findViewById(R.id.tv1);
        tv2 = rootView.findViewById(R.id.tv2);
        tv3 = rootView.findViewById(R.id.tv3);
        tv4 = rootView.findViewById(R.id.tv4);
        tv5 = rootView.findViewById(R.id.tv5);
        tv6 = rootView.findViewById(R.id.tv6);
        tv7 = rootView.findViewById(R.id.tv7);
        tv8 = rootView.findViewById(R.id.tv8);
        tv9 = rootView.findViewById(R.id.tv9);
        tvPlus = rootView.findViewById(R.id.tvPlus);
        tvMinus = rootView.findViewById(R.id.tvMinus);
        tvUmnoj = rootView.findViewById(R.id.tvUmnoj);
        tvDeli = rootView.findViewById(R.id.tvDeli);
        tvRavno = rootView.findViewById(R.id.tvRavno);
        tvPM = rootView.findViewById(R.id.tvPM);
        tvComma = rootView.findViewById(R.id.tvComma);
        tvProc = rootView.findViewById(R.id.tvProc);

        tv1.setOnClickListener(onClickNumber); // устанавливаем обработчик событий onClickNumber на элемент tv1
        tv2.setOnClickListener(onClickNumber);
        tv3.setOnClickListener(onClickNumber);
        tv4.setOnClickListener(onClickNumber);
        tv5.setOnClickListener(onClickNumber);
        tv6.setOnClickListener(onClickNumber);
        tv7.setOnClickListener(onClickNumber);
        tv8.setOnClickListener(onClickNumber);
        tv9.setOnClickListener(onClickNumber);
        tv0.setOnClickListener(onClickNumber);


        tvPlus.setOnClickListener(onClickMath); // устанавливаем обработчик событий onClickMath на элемент tvPlus
        tvMinus.setOnClickListener(onClickMath);
        tvDeli.setOnClickListener(onClickMath);
        tvUmnoj.setOnClickListener(onClickMath);
        tvClean.setOnClickListener(onClickClear);
        tvComma.setOnClickListener(onClickMath);
        tvProc.setOnClickListener(onClickMath);
        tvRavno.setOnClickListener(onClickMath);

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expr = tvExpr.getText().toString();
                int len = expr.length();
                if (len > 0) {
                    expr = expr.substring(0,len - 1);
                    tvExpr.setText(expr);
                }
            }
        });
    }

    View.OnClickListener onClickNumber = new View.OnClickListener() { // объявляем обработчик событий типа View.OnClickListener, т.е. срабатывает при нажатии
        @Override // означает что это основная функция обработчика и без нее он работать не будет
        public void onClick(View v) { // основная функция обработчика
            // принимает переменную типа View (объект Пользовательского Интерфейса) - v , элемент на который нажал пользователь

            String number = ((TextView) v).getText().toString(); // присваиваем переменной number то, что написано на нажатом объекте пользователем
            // в данном конкретном случае мы пишем этот обработчик нажатий только для цифр
            // каждая цифра на ПИ это текстовое поле с соответствующей цифрой на нем

            tvExpr.setText(tvExpr.getText() + "" + number); // задаем текстовому полю текст что был в нем + значение переменной number (нажатую цифру)
            parse(); // вызываем функцию обработки выражения в tvExpr и вычисления ответа
        }
    };

    View.OnClickListener onClickMath = new View.OnClickListener() { // опиши сам что мы тут делаем и для чего оно надо
        @Override
        public void onClick(View v) {
            String simvol = ((TextView) v).getText().toString();
            if (simvol.equals("=")) {
                tvExpr.setText(tvAnswer.getText());
                tvAnswer.setText("");
            } else
                tvExpr.setText(tvExpr.getText() + simvol);
        }
    };

    View.OnClickListener onClickClear = new View.OnClickListener() { // да и это тоже можешь сам описать
        @Override
        public void onClick(View v) {
            tvExpr.setText("");
        }
    };

    private void parse() { // функцию обработки выражения в tvExpr и вычисления ответа
        ArrayList<splitItem> splits = new ArrayList<>();
        ArrayList<splitItem> splits2 = new ArrayList<>();  //объявления переменной содержащей спискок действий калькулятора
        // Типы операций
        // вычитание - 0
        // сложение - 1
        // умножение - 2
        // деление - 3

        // переменную и список можно представить следующим образом
        // splitItem - тип данных, который мы сами написали в классе ниже, одна переменная этого типа состоит из операции и цифры с которой ее надо сдалать

        // splits[0] -> splitItem { oper = 1, num = 22 } // + 22
        // splits[1] -> splitItem { oper = 1, num = 24 } // + 24
        // splits[2] -> splitItem { oper = 2, num = 10 } // * 10
        // splits[3] -> splitItem { oper = 0, num = 45 } // - 45
        // splits[4] -> splitItem { oper = 2, num = 90 } // * 90
        // splits[5] -> splitItem { oper = 0, num = 58 } // - 58
        // splits[6] -> splitItem { oper = 1, num = 21 } // + 21
        // splits[7] -> splitItem { oper = 0, num = 42 } // - 42
        // splits[8] -> splitItem { oper = 3, num = 65 } // / 65

        String expr = tvExpr.getText().toString(); // записываем в переменную expr то, что отображено на экране в текстовом поле tvExpr
        int lastMathOperation = 1; // переменная для хранения типа последнего действия, изначально соответствует типу операции +
        int lastMathOperationPos = 0; // переменная для хранения позиции последнего обработанного математического действия в строке expr
        double answer = 0; // переменная для хранения ответа

        for (int i = 0; i<expr.length(); i++) { //цикл для перебора каждого символа в строке expr
            // i - позиция текущего символа для проверки

            if (expr.charAt(i) == '+') { // условие-сравнения текущего символа строки на соответствие математическому действию +
                String number = expr.substring(lastMathOperationPos == 0 ? 0 : lastMathOperationPos+1, i); // отделяем число перед найденым знаком в отдельную переменную
                // чтобы его отделить, нам необходима последняя и текущая позиции математических операций.
                // Берем то, что между ними начиная с lastMathOperationPos+1 до i не включительно
                //
                //                     last  current
                //                       v    v
                // индекс символа | 0 1 2 3 4 5 6 7 8
                //
                //     пример     | 5 5 - 6 6 + 5 6 7

                splits.add(new splitItem(lastMathOperation, number)); // добавляем в массив splits новую переменную типа splitItem
                // при этом передаем тип последней операции и число, для примера выше это будет 0 и "66" соответствено.
                // тип операции 0, так как прошлая операция была вычитанием

                lastMathOperation = 1; // присваиваем текущий тип операции как последний обработанный
                lastMathOperationPos = i; // присваиваем текущую позицию операции как последнюю обработанную
            }
            if (expr.charAt(i) == '-') {
                splits.add(new splitItem(lastMathOperation, expr.substring(lastMathOperationPos == 0 ? 0 : lastMathOperationPos+1, i)));
                lastMathOperation = 0;
                lastMathOperationPos = i;
            }
            if (expr.charAt(i) == '×') {
                splits.add(new splitItem(lastMathOperation, expr.substring(lastMathOperationPos == 0 ? 0 : lastMathOperationPos+1, i)));
                lastMathOperation = 2;
                lastMathOperationPos = i;
            }
            if (expr.charAt(i) == '÷') {
                splits.add(new splitItem(lastMathOperation, expr.substring(lastMathOperationPos == 0 ? 0 : lastMathOperationPos+1, i)));
                lastMathOperation = 3;
                lastMathOperationPos = i;
            }
        }

        splits.add(new splitItem(lastMathOperation, expr.substring(lastMathOperationPos == 0 ? 0 : lastMathOperationPos+1))); // добавляем последнее число в выражении
        // оно вне цикла т.к. после него нет математической операции и не одно условие в цикле не сработает для его добавления в список операций калькулятора
        //
        //                           last
        //                            v
        // индекс символа | 0 1 2 3 4 5 6 7 8
        //
        //     пример     | 5 5 - 6 6 + 5 6 7

        // теперь наш список операций калькулятора выглядит следующим образом:

        // splits[0] -> splitItem { operation = 1, number =  55 } // + 55
        // splits[1] -> splitItem { operation = 0, number =  66 } // - 66
        // splits[2] -> splitItem { operation = 1, number = 567 } // + 567

        for (splitItem item : splits) { // цикл перебора списка всех операций калькулятора
            // данная запись работает с ограниченным количеством типов списков

            // в данном случае запись стоит понимать так

            // splits - список
            // item - текущий элемент из списка
            // splitItem - тип текущего элемента

            // данную запись можно записать так:

            // for (int i = 0; i < splits.length(); i ++) {

            //    splitItem item = splits.get(i);

            //    ...
            // }

            // что длинно и громозко...

            if (item.operation == 1) { // если тип текущей операции 1 (сложение)
                splits2.add(item);
            }

            if (item.operation == 0) { // если тип текущей операции 0 (вычитание)
                splits2.add(item);
            }

            if (item.operation == 2) { // если тип текущей операции 2 (умножение)
                double lastNumber = splits2.get(splits2.size()-1).number;
                splits2.get(splits2.size()-1).number = lastNumber * item.number;
            }

            if (item.operation == 3) { // если тип текущей операции 3 (деление)
                double lastNumber = splits2.get(splits2.size()-1).number;
                splits2.get(splits2.size()-1).number = lastNumber / item.number;
            }
        }

        for (splitItem item : splits2) {
            if (item.operation == 1) { // если тип текущей операции 1 (сложение)

                // при первой итерации из нашего примера item будет равно splitItem { operation = 1, number =  55 }
                //                                                            ↑↑↑
                // т.е. item.operation - обращение к переменной operation из  ↑↑↑
                //
                answer = answer + item.number; // присваиваем переменной answer сумму (т.к. тип операции сложение) ее самой и текущего числа
            }

            if (item.operation == 0) { // если тип текущей операции 0 (вычитание)

                // при первой итерации из нашего примера item будет равно splitItem { operation = 0, number =  55 }
                //                                                            ↑↑↑
                // т.е. item.operation - обращение к переменной operation из  ↑↑↑
                //
                answer = answer - item.number; // присваиваем переменной answer сумму (т.к. тип операции сложение) ее самой и текущего числа
            }
        }

        if (answer % 1 == 0)
            tvAnswer.setText("" + (int) answer); // передаем текстовому полю tvAnswer значение переменной answer
        else
            tvAnswer.setText("" + answer);
    }

    class splitItem { //Клас типа данных для хранения преобразованных чисел и операций перед ними
        int operation; // тип математической операции
        double number; // число, с которым производится матическая операция

        splitItem(int operation, String number) { //конструктор класа, при создании экземпляра класса принимает в себе тип операции и цифру
            this.operation = operation;
            this.number = Double.parseDouble(number); // переводим строку в число и сохраняем // String "456" => int 456
        }
    }
}
