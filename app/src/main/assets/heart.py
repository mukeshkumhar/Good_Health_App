# %%

# %

# from cgitb import reset


# reset -f


# %%

import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd
import tensorflow as tf

# %%
df =pd.read_csv(r'/Users/mukeshkumhar/Desktop/hog/heart_2020_cleaned.csv')
print(df.head())
print(df.head(319796))


# %%
df.describe()

# %%
sns.pairplot(df,hue='HeartDisease')

# %%
data = df.values
x = data[:,0:]
y = data[:, 17]
print(x)
print(y)

# %%
from sklearn.model_selection import train_test_split
x_train , x_test , y_train , y_test = train_test_split(x ,y , test_size = 0.2)

# %%
print(x_train)

# %%
print(x_test)

# %%
print(y_train)

# %%
print(y_test)

# %%
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import accuracy_score

le = LabelEncoder()

x_train_encoded = x_train.copy()
x_test_encoded = x_test.copy()
for i in range(x_train_encoded.shape[1]):
    if isinstance(x_train_encoded[0, i], str):
        x_train_encoded[:, i] = le.fit_transform(x_train_encoded[:, i])
        x_test_encoded[:, i] = le.transform(x_test_encoded[:, i]) 

model_lr = LogisticRegression()
model_lr.fit(x_train_encoded, y_train)

prediction_lr = model_lr.predict(x_test_encoded)

accuracy_lr = accuracy_score(y_test, prediction_lr) * 100
print(f"Logistic Regression Accuracy: {accuracy_lr:.2f}%")

for i in range(len(prediction_lr)):
    print(y_test[i], prediction_lr[i])

for i in range(len(prediction_lr)):
    if y_test[i] != prediction_lr[i]:
        print(f"Logistic Regression Mismatch at index {i+1}: Actual = {y_test[i]}, Predicted = {prediction_lr[i]}")


# %%
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import accuracy_score

le = LabelEncoder()

x_train_encoded = x_train.copy()
x_test_encoded = x_test.copy()
for i in range(x_train_encoded.shape[1]):
    if isinstance(x_train_encoded[0, i], str): 
        x_train_encoded[:, i] = le.fit_transform(x_train_encoded[:, i])
        x_test_encoded[:, i] = le.transform(x_test_encoded[:, i]) 

model_dt = DecisionTreeClassifier()
model_dt.fit(x_train_encoded, y_train)

prediction_dt = model_dt.predict(x_test_encoded)

accuracy_dt = accuracy_score(y_test, prediction_dt) * 100
print(f"Decision Tree Accuracy: {accuracy_dt:.2f}%")

for i in range(len(prediction_dt)):
    print(y_test[i], prediction_dt[i])

for i in range(len(prediction_dt)):
    if y_test[i] != prediction_dt[i]:
        print(f"Decision Tree Mismatch at index {i+1}: Actual = {y_test[i]}, Predicted = {prediction_dt[i]}")


# %%
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.metrics import accuracy_score

le = LabelEncoder()

x_train_encoded = x_train.copy()
x_test_encoded = x_test.copy()
for i in range(x_train_encoded.shape[1]):
    if isinstance(x_train_encoded[0, i], str):  
        x_train_encoded[:, i] = le.fit_transform(x_train_encoded[:, i])
        x_test_encoded[:, i] = le.transform(x_test_encoded[:, i]) 
scaler = StandardScaler()
x_train_scaled = scaler.fit_transform(x_train_encoded)
x_test_scaled = scaler.transform(x_test_encoded)

knn = KNeighborsClassifier(n_neighbors=5)

knn.fit(x_train_scaled, y_train)

prediction_knn = knn.predict(x_test_scaled)

accuracy_knn = accuracy_score(y_test, prediction_knn) * 100
print(f"KNN Accuracy: {accuracy_knn:.2f}%")

for i in range(len(prediction_knn)):
    if y_test[i] != prediction_knn[i]:
        print(f"KNN Mismatch at index {i+1}: Actual = {y_test[i]}, Predicted = {prediction_knn[i]}")


# %%
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.metrics import accuracy_score, classification_report

le = LabelEncoder()
x_train_encoded = x_train.copy()
x_test_encoded = x_test.copy()
for i in range(x_train_encoded.shape[1]):
    if isinstance(x_train_encoded[0, i], str):  
        x_train_encoded[:, i] = le.fit_transform(x_train_encoded[:, i])
        x_test_encoded[:, i] = le.transform(x_test_encoded[:, i])  


scaler = StandardScaler()
x_train_scaled = scaler.fit_transform(x_train_encoded)
x_test_scaled = scaler.transform(x_test_encoded)
decision_tree = DecisionTreeClassifier()
decision_tree.fit(x_train_scaled, y_train)
prediction_dt = decision_tree.predict(x_test_scaled)

accuracy_dt = accuracy_score(y_test, prediction_dt) * 100
print(f"Decision Tree Accuracy: {accuracy_dt:.2f}%")
print("Decision Tree Classification Report:")
print(classification_report(y_test, prediction_dt))
for i in range(len(prediction_dt)):
    if y_test[i] != prediction_dt[i]:
        print(f"Decision Tree Mismatch at index {i+1}: Actual = {y_test[i]}, Predicted = {prediction_dt[i]}")


# %%
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.metrics import accuracy_score, classification_report
import joblib as jb
import numpy as np
import tensorflow as tf
print(tf.__version__)
# import tensorflow_decision_forests as tfdf

# Encode the x_train and x_test data using LabelEncoder
le = LabelEncoder()

# Assuming x_train and x_test are NumPy arrays
x_train_encoded = x_train.copy()
x_test_encoded = x_test.copy()
for i in range(x_train_encoded.shape[1]):
    if isinstance(x_train_encoded[0, i], str):  # Check if the column contains strings
        le.fit(np.concatenate([x_train_encoded[:, i], x_test_encoded[:, i]]))  # Fit on combined data to ensure consistency
        x_train_encoded[:, i] = le.transform(x_train_encoded[:, i])
        x_test_encoded[:, i] = le.transform(x_test_encoded[:, i])  # Use transform to keep consistency

# Normalize the data
scaler = StandardScaler()
x_train_scaled = scaler.fit_transform(x_train_encoded)
x_test_scaled = scaler.transform(x_test_encoded)

# Applying Decision Tree Classifier
decision_tree = DecisionTreeClassifier()

# Fitting Decision Tree to the scaled dataset
decision_tree.fit(x_train_scaled, y_train)

# Predicting the labels for x_test
prediction_dt = decision_tree.predict(x_test_scaled)

# Calculate and print the accuracy score
accuracy_dt = accuracy_score(y_test, prediction_dt) * 100
print(f"Decision Tree Accuracy: {accuracy_dt:.2f}%")

# Print classification report
print("Decision Tree Classification Report:")
print(classification_report(y_test, prediction_dt))

# Identify and print mismatches between actual and predicted values
for i in range(len(prediction_dt)):
    if y_test[i] != prediction_dt[i]:  # Use direct indexing for NumPy arrays
        print(f"Decision Tree Mismatch at index {i+1}: Actual = {y_test[i]}, Predicted = {prediction_dt[i]}")

# Save the trained model using joblib
with open('decision_tree_model.pkl', 'wb') as file:
    jb.dump(decision_tree, file)

print("Decision Tree model saved to 'decision_tree_model.pkl'")

# convert in to tensanflow lite

# conveter = lite.TocoConverter.from_keras_model_file(keras_file)
# tflite_model = converter.convert()
# open("linear.tflite", "wb").write(tflite_model)

model = tf.keras.models.load_model('/Users/mukeshkumhar/Desktop/hog/heart_2020_cleaned')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# converter = TFLiteConverter.from_keras_model_file(keras_file)
# tflite_model = converter.convert()
open("linear.tflite", "wb").write(tflite_model) 

with open("decision_tree_tflite.tflite", "wb") as f:
    f.write(tflite_model)



