package neurophExample;

import java.util.Random;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.neuroph.nnet.MultiLayerPerceptron;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;


public class NeurophExampleMain {
	
	private static int sign(Random random) {
		return random.nextBoolean() ? 1 : -1;
	}

	public static void main(String[] args) throws FileNotFoundException {
		
        int numInputs = 2;
        int numOutputs = 1;
        int numHiddenNeurons = 3;
        double learningRate = 0.01;
        double maxError = 0.01;
        int maxIterations = 200;
        PrintWriter pw = new PrintWriter("salida.txt"); // Guarda la salida del programa
        PrintWriter pwEntrenamiento = new PrintWriter("Entremamiento.txt"); // Guarda errores de entrenamiento
        PrintWriter pwValidacion = new PrintWriter("Validacion.txt"); // Guarda errores de validacion
        PrintWriter pwX = new PrintWriter("X.txt"); // Guarda la pos X
        PrintWriter pwY = new PrintWriter("Y.txt"); // Guarda la pos Y
        PrintWriter f = new PrintWriter("f.txt"); // Guarda el resultado de la funcion (matematicamente)
        PrintWriter fRed = new PrintWriter("fRed.txt"); // Guarda el resultado de la funcion (hecho por la red)
		
		// Create new simple perceptron network
		MultiLayerPerceptron neuralNetwork = 
				new MultiLayerPerceptron(TransferFunctionType.SIN,
						numInputs, numHiddenNeurons, numOutputs);
		
        BackPropagation backpropagation = neuralNetwork.getLearningRule();
        backpropagation.setLearningRate(learningRate);
        backpropagation.setMaxError(maxError);
        backpropagation.setMaxIterations(maxIterations);
		
		// Create training and validation set
		DataSet trainingSet = new DataSet(2,1);
		DataSet validationSet = new DataSet(2,1);
		
		
		// Añadimos datos al conjunto de entrenamiento
		trainingSet = createDataSet(1000);
		
		// Añadimos datos al conjunto de validación
		validationSet = createDataSet(1000);
		
		
		
        // Entrenar la red neuronal
        for (int epoch = 0; epoch < 30; epoch++) {
            neuralNetwork.learn(trainingSet);

            double trainingError = neuralNetwork.getLearningRule().getPreviousEpochError();
            double validationError = validate(neuralNetwork, validationSet);
            pw.println("Epoca #" + epoch + " - Error entrenamiento: " + trainingError + " - Error validacion: " + validationError);
            pwEntrenamiento.println(trainingError);
            pwValidacion.println(validationError);

            System.out.println("Epoca #" + epoch + " - Error entrenamiento: " + trainingError + " - Error validacion: " + validationError);
        }
		
        
        DataSet testSet = new DataSet(2, 1); // 2 entradas, 1 salida

        double xStep = 2 * Math.PI / 100;
        double yStep = 2 * Math.PI / 100;

        double x = -Math.PI;
        double y = -Math.PI;

        for (int i = 0; i < 10000; i++) {
			double[] input = {x, y};
			double[] output = {Math.sin(x) * Math.cos(y)};
			DataSetRow row = new DataSetRow(input, output);
			testSet.add(row);
			
			y += yStep;
			
			if ((i + 1) % 100 == 0) {
				y = -Math.PI;
				x += xStep;
			}
        }
        
        System.out.println();
        System.out.println("Set de pruebas:");
        System.out.println();
        pw.println();
		pw.println("Set de pruebas:");
		pw.println();
        
        // Entrenar red de prueba
        neuralNetwork.learn(testSet);
        
        
        // Para generar el grafico 3D, guardamos x,y, y el resultado 
        // de la funcion que debería dar y el que da la red neuronal
        for(DataSetRow row : testSet.getRows()) {
        	neuralNetwork.setInput(row.getInput());
        	pwX.println(row.getInput()[0]);
        	pwY.println(row.getInput()[1]);
        	f.println(row.getDesiredOutput()[0]);
            neuralNetwork.calculate();
            double[] output = neuralNetwork.getOutput();
            fRed.println(output[0]);
        }

        double ECM = validate(neuralNetwork, testSet);

        System.out.println("- Error de prueba: " + ECM);
        pw.println("- Error de prueba: " + ECM);

      pw.close();
      pwEntrenamiento.close();
      pwValidacion.close();
      pwX.close();
      pwY.close();
      f.close();
      fRed.close();
        
	}
	
    private static double validate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet validationSet) {
        double error = 0;

        for (DataSetRow row : validationSet.getRows()) {
            neuralNetwork.setInput(row.getInput());
            neuralNetwork.calculate();
            double[] output = neuralNetwork.getOutput();
            error += Math.pow(row.getDesiredOutput()[0] - output[0], 2);
        }

        error /= validationSet.size();

        return error;
    }
    
    
    private static DataSet createDataSet(int size) {
        DataSet dataSet = new DataSet(2, 1);
        Random random = new Random();

        for(int i = 0; i < size; i++) {
			double x = random.nextDouble()*Math.PI * sign(random);
			double y = random.nextDouble()*Math.PI * sign(random);
			double f = Math.sin(x) * Math.cos(y);
			dataSet.add(new DataSetRow(new double[] {x,y}, new double [] {f}));
		}
        

        return dataSet;
    }

}
