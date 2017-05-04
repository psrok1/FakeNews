import sys
import numpy as np

import pickle
import json

from sklearn.ensemble import GradientBoostingClassifier
from feature_engineering import refuting_features, polarity_features, hand_features
from feature_engineering import word_overlap_features

from utils.score import LABELS


def classify(headline, body, clf):
    h = [headline]
    b = [body]

    X_overlap = word_overlap_features(h, b)
    X_refuting = refuting_features(h, b)
    X_polarity = polarity_features(h, b)
    X_hand = hand_features(h, b)

    X = np.c_[X_hand, X_polarity, X_refuting, X_overlap]

    predicted = [LABELS[int(a)] for a in clf.predict(X)]

    return predicted


if __name__ == "__main__":

    line_generator = sys.stdin

    with open('fake_news_classifier.pkl', 'rb') as fid:
        best_fold = pickle.load(fid)

    for line in line_generator:
        news = json.loads(line)
        try:
            [result] = classify(news["heading"], news["article"], best_fold)

            news["result"] = result

            print(json.dumps(news))
            sys.stdout.flush()
        except KeyError as err:
            print("Key error in json object: {0}".format(err))


