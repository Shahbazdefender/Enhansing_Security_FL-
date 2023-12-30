package com.zeroinfinity.federatedclient.model.ml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AllModelResponse {

	private KnnModel knn;
	private DecisionTreeModel decision_tree;
	private RandomForestModel random_forest;
	private SvmModel svm;
	private NaiveBayesModel naive_bayes;

	public KnnModel getKnn() {
		return knn;
	}

	public void setKnn(KnnModel knn) {
		this.knn = knn;
	}

	public DecisionTreeModel getDecision_tree() {
		return decision_tree;
	}

	public void setDecision_tree(DecisionTreeModel decision_tree) {
		this.decision_tree = decision_tree;
	}

	public RandomForestModel getRandom_forest() {
		return random_forest;
	}

	public void setRandom_forest(RandomForestModel random_forest) {
		this.random_forest = random_forest;
	}

	public SvmModel getSvm() {
		return svm;
	}

	public void setSvm(SvmModel svm) {
		this.svm = svm;
	}

	public NaiveBayesModel getNaive_bayes() {
		return naive_bayes;
	}

	public void setNaive_bayes(NaiveBayesModel naive_bayes) {
		this.naive_bayes = naive_bayes;
	}

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}

}
