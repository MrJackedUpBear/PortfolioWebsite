// See https://aka.ms/new-console-template for more information
using System.ComponentModel.DataAnnotations;
using System.Diagnostics;
using System.Net.WebSockets;
using System.Numerics;
using System.Runtime.InteropServices;

class Program{
    static void Main(String[] args){
        GetInputs();
        Console.WriteLine("Press any key to exit.");
        Console.ReadKey();
    }

    static void GetInputs(int j = 0){
        if (j == 0){
            Console.WriteLine("Calculator in C#");
            Console.WriteLine("This is a simple calculator that supports basic arithmetic operations and some advanced functions.");
            Console.WriteLine("Welcome to the Calculator!");
            Console.WriteLine("This calculator supports the following operations:");
            Console.WriteLine("Addition (+), Subtraction (-), Multiplication (*), Division (/), Modulus (%), Power (^), Square Root (sqrt), Cube Root (cbrt), Factorial (!), Prime (prime), Fibonacci (fib), LCM (lcm), HCF (hcf), Logarithm (log, log10, log2), Trigonometric Functions (sin, cos, tan, sinh, cosh, tanh, asin, acos, atan, asinh, acosh, atanh), Rounding Functions (round, floor, ceil), Absolute Value (abs), Min (min), Max (max)");
            Console.WriteLine("You can also use parentheses to group operations, e.g., (2 + 3) * 4 or 2 * (3 + 4).");
            Console.WriteLine("You can use multiple operations in a single expression, e.g., 2 + 3 * 4 - 5 / 6.");
        }

        Console.WriteLine();
        Console.WriteLine("Enter an equation or type exit to quit.");
        String input;
        input = Console.ReadLine();

        input = input.ToLower();

        if (input == "exit"){
            Console.WriteLine("You used the calculator " + j + " time(s).");
            Console.WriteLine("Thank you for using the calculator!");
            return;
        }
        else if (input.Length < 2){
            Console.WriteLine("Invalid input. Please try again.");
            j++;
            GetInputs(j);
            return;
        }

        List<String> inputs = ParseInputs(input);

        if (!IsValidInput(inputs)){
            Console.WriteLine("Invalid input. Please try again.");
            j++;
            GetInputs(j);
            return;
        }

        Interpreter(inputs, inputs.Count);
        Console.WriteLine("The result is: " + inputs[0]);

        j+= 1;
        GetInputs(j);
    }

    public static String SetInputs(String input){
        List<String> inputs = ParseInputs(input);
        Interpreter(inputs, inputs.Count);

        return inputs[0];
    }

    static Boolean IsValidInput(List<String> inputs){
        List<String> validOperators = ["-","+","*","/","(",")","!","^", "%", "x"];
        validOperators.Add("fib");

        String validNum = "0123456789-1-2-3-4-5-6-7-8-9";

        Boolean isValid = false;

        for (int i = 0; i < inputs.Count; i++){
            if (validOperators.Contains(inputs[i])){
                if (i - 1 >= 0){
                    if (validOperators.Contains(inputs[i - 1]) && !(inputs[i - 1] == "!" || inputs[i - 1] == "(" || inputs[i - 1] == ")") && !(inputs[i] == "(" || inputs[i] == ")")){
                        return false;
                    }
                    else{
                        isValid = true;
                    }
                }
                else{
                    isValid = true;
                }
            }
            else{
                int numPeriods = 0;
                for (int k = 0; k < inputs[i].Length; k++){
                    if (validNum.Contains(inputs[i][k])){
                        isValid = true;
                        
                    }
                    else if (inputs[i][k] == '.'){
                        numPeriods++;

                        if (numPeriods == 1){
                            isValid = true;
                        }
                        else{
                            isValid = false;
                            k = inputs[i].Length;
                        }
                    }
                    else{
                        isValid = false;
                        k = inputs[i].Length;
                    }
                }
            }

            if (isValid && i != (inputs.Count - 1)){
                isValid = false;
            }
            else{
                return isValid;
            }
        }

        return false;
    }

    static List<String> ParseInputs(String input){
        List<String> inputs = [];

        input = input.Replace(" ", "");
        int len = input.Length;

        for (int i = 0; i < len; i++){
            switch(IsNum(input[i])){
                case true:
                    String temp = "";
                    int j = i;
                    i++;
                    temp += input[i - 1];

                    if (temp == "-" && inputs.Count > 0 && input[i] != '('){
                        inputs.Add("+");
                    }
                    else if (temp == "-" && inputs.Count == 0 && input[i] == '('){
                        inputs.Add("-1");
                        inputs.Add("*");
                    }
                    else if (temp == "-" && inputs.Count > 0 && input[i] == '('){
                        inputs.Add("+");
                        inputs.Add("-1");
                        inputs.Add("*");
                    }

                    while (i < len && IsNum(input[i]) && input[i] != '-'){
                        temp += input[i];
                        i++;
                    }

                    if (temp != "-"){
                        inputs.Add(temp);
                    }

                    i = j + temp.Length - 1;
                    break;
                case false:
                    switch(IsLetter(input[i])){
                        case true:
                            temp = "";
                            while (i < len && IsLetter(input[i])){
                                temp += input[i];
                                i++;
                            }
                            i--;

                            inputs.Add(temp);
                            break;
                        default:
                            if (input[i] != ' '){
                                inputs.Add(input[i].ToString());
                            }
                            break;
                    }
                    break;
            }
        }

        return inputs;
    }

    static Boolean IsLetter(char input){
        if (input >= 'a' && input <= 'z')
        {
            return true;
        }

        return false;
    }

    static Boolean IsNum(char input){
        if ((input >= '0' && input <= '9') || input == '-' || input == '.'){
            return true;
        }
        return false;
    }

    static void Interpreter(List<String> inputs, int len){
        int open = 0, close = 0;

        for (int i = 0; i < len; i++){
            switch(inputs[i]){
                case "(":
                    open++;
                    break;
                case ")":
                    close++;
                    break;
            }
        }

        if (open != close){
            Console.WriteLine("Invalid Expression");
            return;
        }

        len = inputs.Count;

        switch (open){
            case 0:
                OrderOfOperations(inputs, len);
                break;
            default:
                ParenthesisChecker(inputs, len, open);
                break;
        }

        len = inputs.Count;

        while (len != 1){
            OrderOfOperations(inputs, len);
            len = inputs.Count;
        }
    }

    static void OrderOfOperations(List<String> inputs, int len, int start = 0, int end = 0){
        double a = 0, b = 0;
        String op = "";

        if (end == 0){
            end = len;
        }

        for (int i = 0; i < len; i++){
            if (inputs[i] == "fib"){
                op = inputs[i];
                if (i + 1 < len){
                    a = double.Parse(inputs[i + 1]);
                }
                String temp = Calculate(a, 0, op);

                len = inputs.Count;
                for (int j = 0; j < len; j++){
                    inputs.RemoveAt(0);
                }
                
                Console.WriteLine("Since you included fib, we will only calculate the fibinocci sequence.");
                inputs.Add(temp);
                return;
            }
        }

        for (int i = start; i < end; i++){
            if (inputs[i] == "^"){
                op = inputs[i];
                a = 0;
                b = 0;

                if (i - 1 >= 0){
                    a = double.Parse(inputs[i - 1]);
                }

                if (i + 1 < end){
                    b = double.Parse(inputs[i + 1]);
                }

                a = double.Parse(Calculate(a, b, op));

                inputs[i - 1] = a.ToString();

                inputs.RemoveAt(i + 1);
                inputs.RemoveAt(i);
                end -= 2;
                len = inputs.Count;

                if (i == end && end > 1 && end < len){
                    if (inputs[i] == ")" && inputs[i - 2] == "("){
                        inputs.RemoveAt(i);
                        inputs.RemoveAt(i - 2);
                        end -= 2;
                    }
                }
                i = start;
            }
        }

        for (int i = start; i < end; i++){
            if (inputs[i] == "!"){
                op = inputs[i];
                a = 0;
                if (i - 1 >= 0){
                    a = double.Parse(inputs[i - 1]);
                }

                a = double.Parse(Calculate(a, 0, op));
                inputs[i-1] = a.ToString();

                inputs.RemoveAt(i);
                end--;
                len = inputs.Count;
                if (i == end && end > 1 && end < len){
                    if (inputs[i] == ")" && inputs[i - 2] == "("){
                        inputs.RemoveAt(i);
                        inputs.RemoveAt(i - 2);
                        end -= 2;
                    }
                }
                i = start;
            }
        }

        for (int i = start; i < end; i++){
            if (inputs[i] == "*" || inputs[i] == "/" || inputs[i] == "%" || inputs[i] == "x"){
                op = inputs[i];
                a = 0;
                b = 0;
                if (i - 1 >= 0){
                    a = double.Parse(inputs[i - 1]);
                }
                if (i + 1 < end){
                    b = double.Parse(inputs[i + 1]);
                }

                if (inputs[i] == "/"){
                    double num = double.Parse(Calculate(a, b, op));
                    inputs[i - 1] = num.ToString();
                }
                else{
                    a = double.Parse(Calculate(a, b, op));
                    inputs[i - 1] = a.ToString();
                }

                inputs.RemoveAt(i + 1);
                inputs.RemoveAt(i);
                end -= 2;
                len = inputs.Count;

                if (i == end && end > 1 && end < len){
                    if (inputs[i] == ")" && inputs[i - 2] == "("){
                        inputs.RemoveAt(i);
                        inputs.RemoveAt(i - 2);
                        end -= 2;
                    }
                }
                i = start;
            }
        }

        for (int i = start; i < end; i++){
            if (inputs[i] == "+" || inputs[i] == "-"){
                a = 0;
                b = 0;
                op = inputs[i];
                if (i - 1 >= 0){
                    a = double.Parse(inputs[i - 1]);
                }
                if (i + 1 < end){
                    b = double.Parse(inputs[i + 1]);
                }
                a = double.Parse(Calculate(a, b, op));

                inputs[i - 1] = a.ToString();

                inputs.RemoveAt(i + 1);
                inputs.RemoveAt(i);
                end -= 2;
                len = inputs.Count;

                if (i == end && end > 1 && end < len){
                    if (inputs[i] == ")" && inputs[i - 2] == "("){
                        inputs.RemoveAt(i);
                        inputs.RemoveAt(i - 2);
                        end -= 2;
                    }
                }
                i = start;
            }
        }

        if (len == 3){
            if (inputs[0] == "(" && inputs[2] == ")"){
                inputs.RemoveAt(0);
                inputs.RemoveAt(1);
            }
        }
    }

    static void ParenthesisChecker(List<String> inputs, int len, int parenthesis){
        int open = 0, close = 0;

        for (int l = 0; l < parenthesis; l++){
            len = inputs.Count;
            for (int i = 0; i < len; i++){
                if (inputs[i].Contains('(')){
                    int j = i + 1;
                    open = i;
                    
                    while (j < len){
                        if (inputs[j].Contains(')')){
                            close = j;
                            break;
                        }
                        else if (inputs[j].Contains('(')){
                            open = j;
                        }
                        j++;
                    }
                    OrderOfOperations(inputs, len, open, close);
                    i = len;
                }
            }
        }
    }

    static void ScientificNotation(){
        
    }

    static String Calculate(double a, double b, String op){
        String num = "";
        switch(op){
            case "+":
                num = Add(a, b);
                break;
            case "-":
                num = Subtract(a, b);
                break;
            case "*": case "x":
                num = Multiply(a, b);
                break;
            case "/":
                num = Divide(a, b);
                break;
            case "%":
                num = Modulus(a, b);
                break;
            case "^":
                num = Power(a, b);
                break;
            case "sqrt":
                SquareRoot(a);
                break;
            case "cbrt":
                CubeRoot(a);
                break;
            case "!":
                num = Factorial(a);
                break;
            case "prime":
                Prime(a);
                break;
            case "fib":
                num = Fibonacci(a);
                break;
            case "lcm":
                LCM(a, b);
                break;
            case "hcf":
                HCF(a, b);
                break;
            case "log":
                Log(a);
                break;
            case "log10":
                Log10(a);
                break;
            case "log2":
                Log2(a);
                break;
            case "sin":
                Sin(a);
                break;
            case "cos":
                Cos(a);
                break;
            case "tan":
                Tan(a);
                break;
            case "sinh":
                Sinh(a);
                break;
            case "cosh":
                Cosh(a);
                break;
            case "tanh":
                Tanh(a);
                break;
            case "asin":
                ASin(a);
                break;
            case "acos":
                ACos(a);
                break;
            case "atan":
                ATan(a);
                break;
            case "asinh":
                ASinh(a);
                break;
            case "acosh":
                ACosh(a);
                break;
            case "atanh":
                ATanh(a);
                break;
            case "round":
                Round(a);
                break;
            case "floor":
                Floor(a);
                break;
            case "ceil":
                Ceiling(a);
                break;
            case "abs":
                Abs(a);
                break;
            case "min":
                Min(a, b);
                break;
            case "max":
                Max(a, b);
                break;
            default:
                Console.WriteLine("Invalid Operator");
                break;
        }
        return num;
    }

    static String Add(double a, double b){
        Console.WriteLine(a + b);
        return (a + b).ToString();
    }

    static String Subtract(double a, double b){
        Console.WriteLine(a - b);
        return (a - b).ToString();
    }

    static String Multiply(double a, double b){
        Console.WriteLine(a * b);
        return (a * b).ToString();
    }

    static String Divide(double a, double b){
        if (b == 0){
            Console.WriteLine("Cannot divide by zero");
            return "1";
        }

        double quotient = a / b;

        return quotient.ToString();
    }

    static String Modulus(double a, double b){
        Console.WriteLine(a % b);

        return (a % b).ToString();
    }

    static String Power(double a, double b){
        Boolean isNeg = false;
        int negOrPos = 0;
        if (a < 0){
            isNeg = true;
        }
        Console.WriteLine(Math.Pow(a, b));

        if (isNeg){
            negOrPos = -1;
        }
        else{
            negOrPos = 1;
        }

        return (negOrPos * (int)Math.Pow(a, b)).ToString();
    }

    static void SquareRoot(double a){
        Console.WriteLine(Math.Sqrt(a));
    }

    static void CubeRoot(double a){
        Console.WriteLine(Math.Pow(a, 1.0/3));
    }

    static String Factorial(double a){
        ulong fact = 1;
        for (int i = 1; i <= a; i++){
            fact *= (ulong)i;
        }
        Console.WriteLine(fact);
        return fact.ToString();
    }

    static void Prime(double a){
        bool isPrime = true;
        for (int i = 2; i < a; i++){
            if (a % i == 0){
                isPrime = false;
                break;
            }
        }
        if (isPrime){
            Console.WriteLine("Prime");
        } else {
            Console.WriteLine("Not Prime");
        }
    }

    static String Fibonacci(double n){
        int a = 0, b = 1, c = 0;
        String temp = "";
        for (int i = 0; i < n; i++){
            Console.WriteLine(a);
            temp += a;
            if (i != n - 1){
                temp += ", ";
            }
            c = a + b;
            a = b;
            b = c;
        }
        return temp;
    }

    static void LCM(double a, double b){
        double max = Math.Max(a, b);
        double lcm = max;
        while (true){
            if (lcm % a == 0 && lcm % b == 0){
                Console.WriteLine(lcm);
                break;
            }
            lcm += max;
        }
    }

    static void HCF(double a, double b){
        double min = Math.Min(a, b);
        double hcf = min;
        while (min > 0){
            if (a % min == 0 && b % min == 0){
                Console.WriteLine(min);
                break;
            }
            min--;
        }
    }

    static void Log(double a){
        Console.WriteLine(Math.Log(a));
    }

    static void Log10(double a){
        Console.WriteLine(Math.Log10(a));
    }

    static void Log2(double a){
        Console.WriteLine(Math.Log2(a));
    }

    static void Sin(double a){
        Console.WriteLine(Math.Sin(a));
    }

    static void Cos(double a){
        Console.WriteLine(Math.Cos(a));
    }

    static void Tan(double a){
        Console.WriteLine(Math.Tan(a));
    }

    static void Sinh(double a){
        Console.WriteLine(Math.Sinh(a));
    }

    static void Cosh(double a){
        Console.WriteLine(Math.Cosh(a));
    }

    static void Tanh(double a){
        Console.WriteLine(Math.Tanh(a));
    }

    static void ASin(double a){
        Console.WriteLine(Math.Asin(a));
    }

    static void ACos(double a){
        Console.WriteLine(Math.Acos(a));
    }

    static void ATan(double a){
        Console.WriteLine(Math.Atan(a));
    }

    static void ASinh(double a){
        Console.WriteLine(Math.Asinh(a));
    }

    static void ACosh(double a){
        Console.WriteLine(Math.Acosh(a));
    }   

    static void ATanh(double a){
        Console.WriteLine(Math.Atanh(a));
    }

    static void Round(double a){
        Console.WriteLine(Math.Round(a));
    }

    static void Floor(double a){
        Console.WriteLine(Math.Floor(a));
    }

    static void Ceiling(double a){
        Console.WriteLine(Math.Ceiling(a));
    }

    static void Abs(double a){
        Console.WriteLine(Math.Abs(a));
    }

    static void Min(double a, double b){
        Console.WriteLine(Math.Min(a, b));
    }

    static void Max(double a, double b){
        Console.WriteLine(Math.Max(a, b));
    }
}
