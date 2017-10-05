package com.garkin.calculater.calculater;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Создание кнопок
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnEquals, btnDivide, btnPlus, btnStar, btnMinus, btnC, btnArrow, btnM, btnMC;

    EditText mainEditText;
    //В эту кнопку помещается операция(-, +, *, /) при нажатии на эту кнопку
    Button effectButton;

    Long first, second, valueM;
    boolean showResultBeforeEnterChar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainEditText = (EditText) findViewById(R.id.mainEditText);

        //Присвоение кнопкам, объектов из XML файла
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btnC = (Button) findViewById(R.id.btnC);
        btnEquals = (Button) findViewById(R.id.btnEquals);
        btnDivide = (Button) findViewById(R.id.btnDivine);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnStar = (Button) findViewById(R.id.btnStar);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnArrow = (Button) findViewById(R.id.btnArrow);

        btnM = (Button) findViewById(R.id.btnM);
        btnMC = (Button) findViewById(R.id.btnMC);

        //Присваиваем кнопкам, обработчики нажатий(тоесть,
        // если на кнопку нажать, то у неё сработает слбытие, что её нажали)
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnEquals.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnArrow.setOnClickListener(this);
        btnM.setOnClickListener(this);
        btnMC.setOnClickListener(this);
    }

    //Проверяет пустая ли строка для заполнения
    private boolean checkToEmptyStr(){
        return mainEditText == null || mainEditText.getText().length() <= 0;
    }

    //В зависимости от условий, делает определенные действия
    private void canOperation(Button button){
        //Если текстовое поле не пустое
        if (mainEditText != null) {
            //Если первая переменная отсутсвует, а числовое поле пустое, то мы меняем операцию
            if (mainEditText.getText().length() == 0 && first != null) {
                setEffectButton(button);
            } else if (mainEditText.getText().length() > 0 && first == null) {//Если числовое поле не пустое, а первая переменная отсутсвует, то мы её заполняем
                setEffectButton(button);
                setFirstValue();
            } else if (mainEditText.getText().length() > 0 && first != null && !showResultBeforeEnterChar) { //Подсчет результата!(т.к. числовое поле не пустое, а первая переменная заполнена)
                Long result = calc();
                if (result != null) {
                    first = result;
                    second = null;
                    showResultBeforeEnterChar = true;
                    setEffectButton(button);
                } else {//Очистка переменных
                    resetEffectButton();
                    resetVariable();
                }
            } else if (mainEditText.getText().length() > 0 && first != null && showResultBeforeEnterChar) {
                //Перестановка кнопки операции
                setEffectButton(button);
            }
        }
    }

    //Операции М и МС
    private void canOtherOperation(Button button){
        //Если не пустое поле ввода
        if(!checkToEmptyStr()){
            //Текущее значени из поля ввода
            Long valueNow = Long.parseLong(mainEditText.getText().toString());
            //Получаем Id кнопки
            int btnId = button.getId();

            switch (btnId) {
                case R.id.btnM://Если это М
                    if (valueM == null) {
                        valueM = valueNow;//Место хранения М числа
                        Toast.makeText(getApplicationContext(), "Число " + valueM + " сохранено", Toast.LENGTH_LONG).show();
                    } else {
                        mainEditText.getText().clear();//Очищаем поле ввода
                        mainEditText.getText().append(valueM.toString());//Выводим это значение в поля ввода
                    }
                    break;
                case R.id.btnMC://Если это МС
                    if (valueM != null) {
                        mainEditText.getText().clear();//Очищаем поле ввода
                        mainEditText.getText().append(valueM.toString());//Выводим это значение в поля ввода
                        Toast.makeText(getApplicationContext(), "Число " + valueM + " выгружено из памяти", Toast.LENGTH_LONG).show();
                        valueM = null;//Очищаем это значение
                    }
                    break;
            }

        }
    }

    //Предназначена чтобы убрать эффект затемнения(эффект нажатия) с одной кнопки и назначить другой
    private void setEffectButton(Button button){
        resetEffectButton();
        effectButton = button;
        effectButton.getBackground().setColorFilter(0xFB363636, PorterDuff.Mode.MULTIPLY);
    }

    //Предназначена чтобы убрать эффект затемнения с кнопки, на которой он сейчас
    private void resetEffectButton(){
        if(effectButton != null){
            effectButton.getBackground().setColorFilter(0xFFD7D8D8, PorterDuff.Mode.MULTIPLY);
        }
    }

    //Обнуляет все значения(используемые для вычисления)
    private void resetVariable(){
        effectButton = null;
        first = null;
        second = null;
    }

    //Устанавливает первоначальные значения
    private void setFirstValue(){
        //Преобразует строку с числами, к числу
        first = Long.parseLong(mainEditText.getText().toString());
        //Очищаем строку
        mainEditText.getText().clear();
    }

    //Производим сложение, вычитание, умножение, деление
    private Long calc(){
        Long result = 0L;
        try{
            //Если первое значение не пустое(тоесть мы ввели число и нажали какую-то операцию)
            //Кнопка математической операции не пустая(была выбрана какая-то операция)
            if(first != null && effectButton != null){
                //Заполняем второе значение
                second = Long.parseLong(mainEditText.getText().toString());

                //Очищаем текст
                mainEditText.getText().clear();
                int btnId = effectButton.getId();

                //Считываем что это была за операция +, -, *, /
                switch (btnId){
                    case R.id.btnPlus: result = first + second; break;
                    case R.id.btnMinus: result = first - second; break;
                    case R.id.btnStar: result = first * second; break;
                    case R.id.btnDivine: //Если деление проверяем, не является ли второе значение 0
                        if(second == 0){
                            //Вызываем ошибку
                            throw new ArithmeticException();
                        }
                        result = first / second;
                        break;
                }
                //Записываем результат в числовую строку
                mainEditText.getText().append(result.toString());
            }
            return result;
        } catch (ArithmeticException e){ //Ловим ошибку
            //Всплывающее уведомление,что на ноль делить нельзя
            Toast.makeText(getApplicationContext(),"На ноль делить нельзя!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //Подсчет значений - это кнопка =
    private void calculateValues(){
        //Проверяем не пустое ли, поле ввода
        if(mainEditText.getText().length() <= 0){
            return;
        }
        calc();
        resetEffectButton();
        resetVariable();
    }

    //Очищаем все - это кнопка С
    private void clearAll(){
        if(!checkToEmptyStr()){
            mainEditText.getText().clear();
        }
        resetEffectButton();
        resetVariable();
    }

    //Обновляет значение в переменной first
    private void updateFirstValue(){
        if(showResultBeforeEnterChar){
            first = Long.parseLong(mainEditText.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        Button clickBtn = (Button) view;
        int id = clickBtn.getId();
        switch (id){
            //Если нажали кнопку =
            case R.id.btnEquals:
                calculateValues();
                break;

            //Если нажали кнопку С
            case R.id.btnC:
                clearAll();
                break;

            //Если нажали кнопку <-
            case R.id.btnArrow:
                if(!checkToEmptyStr()){
                    int textLenght = mainEditText.getText().length();
                    mainEditText.getText().delete(textLenght - 1, textLenght);
                    updateFirstValue();
                }
                break;

            //Если нажали кнопку +, -, *, /
            case R.id.btnPlus:
            case R.id.btnMinus:
            case R.id.btnStar:
            case R.id.btnDivine:
                canOperation(clickBtn);
                break;

            //Если нажали кнопку М или МС
            case R.id.btnM:
            case R.id.btnMC:
                canOtherOperation(clickBtn);
                break;

            //Если нажали кнопку 0-9
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
                if(showResultBeforeEnterChar){
                    mainEditText.getText().clear();
                    showResultBeforeEnterChar = false;
                }
                mainEditText.getText().append(clickBtn.getText().toString());
                break;
        }
    }
}
