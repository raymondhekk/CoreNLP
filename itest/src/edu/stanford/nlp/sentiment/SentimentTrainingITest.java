package edu.stanford.nlp.sentiment;

import junit.framework.TestCase;

import java.util.List;
import edu.stanford.nlp.trees.Tree;

/**
 * Tests that the gradient check for the sentiment tool passes.
 */
public class SentimentTrainingITest extends TestCase {
  static final String TRAIN_PATH = "projects/core/data/edu/stanford/nlp/sentiment/onesent.txt";

  public void testGradientCheck() {
    List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(TRAIN_PATH);
    RNNOptions op = new RNNOptions();
    op.numHid = 5;
    SentimentModel model = new SentimentModel(op, trainingTrees);
    assertTrue("Gradient check failed with random seed of " + op.randomSeed, 
               SentimentTraining.runGradientCheck(model, trainingTrees));
  }

  /**
   * Because the regularizations are typically set to be 0.001 of the
   * total cost, it is important to test those gradients with the reg
   * values turned up a lot.
   */
  public void testRegularizationGradientCheck() {
    List<Tree> trainingTrees = SentimentUtils.readTreesWithGoldLabels(TRAIN_PATH);
    RNNOptions op = new RNNOptions();
    op.numHid = 5;
    op.trainOptions.regTransformMatrix = 10.0;
    op.trainOptions.regTransformTensor = 10.0;
    op.trainOptions.regClassification = 10.0;
    op.trainOptions.regWordVector = 10.0;
    SentimentModel model = new SentimentModel(op, trainingTrees);
    assertTrue("Gradient check failed with random seed of " + op.randomSeed, 
               SentimentTraining.runGradientCheck(model, trainingTrees));
  }
}
