def hello(a,b):
    return "Hi from hello"+str(a)+str(b)

"""" importing packages
import pandas as pd

df = pd.DataFrame([
    [18, 110, 18.9, 1400],
    [36, 905, 23.4, 1800],
    [23, 230, 14.0, 1300],
    [60, 450, 13.5, 1500]],

    columns=['Temp', 'Brightness',
             'Humidity', 'Duration'])

df_max_scaled = df.copy()

for column in df_max_scaled.columns:
    df_max_scaled[column] = df_max_scaled[column]  / df_max_scaled[column].abs().max()
"""