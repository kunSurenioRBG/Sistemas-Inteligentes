# Linear Regression

import pandas as pd
import numpy as mp
import matplotlib as plt

def calculate_model(w,b,x):
    # This function allows calculating each of the data resulting from the regression.
    return w*x+b

def calculate_error(y,y_):
    N = y.sh