package com.zeroinfinity.federatedserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zeroinfinity.federatedserver.model.ml.AllModelResponse;
import com.zeroinfinity.federatedserver.model.ml.DecisionTreeModel;
import com.zeroinfinity.federatedserver.model.ml.KnnModel;
import com.zeroinfinity.federatedserver.model.ml.NaiveBayesModel;
import com.zeroinfinity.federatedserver.model.ml.RandomForestModel;
import com.zeroinfinity.federatedserver.model.ml.SvmModel;
import com.zeroinfinity.federatedserver.model.ml.TrainTestData;

@Component
public class MlModelUtil {
	private static final Logger LOG = LoggerFactory.getLogger(MlModelUtil.class);

	public AllModelResponse getAllModel() {
		LOG.info("method: getAllModel");

		AllModelResponse allModel = new AllModelResponse();

		TrainTestData trainTest = new TrainTestData();
		trainTest.setRandom_state("42");
		trainTest.setTest_size("0.3");

		KnnModel knn = new KnnModel();
		knn.setName("KNN");
		knn.setTrain_test_data(trainTest);

		DecisionTreeModel decisionTree = new DecisionTreeModel();
		decisionTree.setName("Decision Tree");
		decisionTree.setTrain_test_data(trainTest);

		RandomForestModel randomForest = new RandomForestModel();
		randomForest.setName("Random Forest");
		randomForest.setTrain_test_data(trainTest);

		SvmModel svm = new SvmModel();
		svm.setName("SVM");
		svm.setTrain_test_data(trainTest);

		NaiveBayesModel naiveBayes = new NaiveBayesModel();
		naiveBayes.setName("Naive Bayes");
		naiveBayes.setTrain_test_data(trainTest);

		allModel.setKnn(knn);
		allModel.setDecision_tree(decisionTree);
		allModel.setRandom_forest(randomForest);
		allModel.setSvm(svm);
		allModel.setNaive_bayes(naiveBayes);

		return allModel;

	}

	public Object getSpecificModel(String modelName) {
		LOG.info("method: getSpecificModel - model: {}", modelName);

		Object result = null;

		TrainTestData trainTest = new TrainTestData();
		trainTest.setRandom_state("42");
		trainTest.setTest_size("0.3");

		KnnModel knn = new KnnModel();
		knn.setName("KNN");
		knn.setTrain_test_data(trainTest);

		DecisionTreeModel decisionTree = new DecisionTreeModel();
		decisionTree.setName("Decision Tree");
		decisionTree.setTrain_test_data(trainTest);

		RandomForestModel randomForest = new RandomForestModel();
		randomForest.setName("Random Forest");
		randomForest.setTrain_test_data(trainTest);

		SvmModel svm = new SvmModel();
		svm.setName("SVM");
		svm.setTrain_test_data(trainTest);

		NaiveBayesModel naiveBayes = new NaiveBayesModel();
		naiveBayes.setName("Naive Bayes");
		naiveBayes.setTrain_test_data(trainTest);

		switch (modelName) {
		case "knn":
			result = knn;
			break;

		case "decisionTree":
			result = decisionTree;
			break;

		case "randomForest":
			result = randomForest;
			break;

		case "svm":
			result = svm;
			break;

		case "naiveBayes":
			result = naiveBayes;
			break;

		default:
			break;
		}

		return result;

	}

	public String incrementVersion(String version) {
		// Extract the numeric part of the version string
		int numPart = Integer.parseInt(version.substring(7));

		// Increment the numeric part by 1
		numPart++;

		// Construct the new version string
		return "version" + numPart;
	}
}
