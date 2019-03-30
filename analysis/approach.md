
Looking for simplest anomaly detection algorithm - series of observations, characterize what is normal and then what falls out of normal. any data points away from a standard distribution are an anomaly (time series analysis allow for more complex anomaly detection)

**Simple Frequency Threshold Exception** within a measured batch of data or duration of time

**Simple Rolling Average / Freqency deviation Exception** store a rolling average for all or each subnet or ip 

**Spark SQL Approach** - create rolling average over overlapping batched windows.  

**Spark Time Series Approach** - map "event data" (that is data with a timestamp about an event but not something that is measured on a regular interval) into tumbling windows and look for frequencies and anomalies and trends.  
this type of analyis will look to the past (recent past) rather than looking to the future  

- **For doing a frequency by ip (or subnet)**
univariate data set might be - timestamp, ip  
- **For doing some type of multi-variable analysis**
multivariate data set might be - timestamp, ip, uri, http_method, http_response_code, etc


**Spark ML Approach**
