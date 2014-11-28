sortedList = importdata('tmp/TermsSortedByNumberOfTweets.csv');




axis = 1:length(sortedList.data);
plot(axis,sortedList.data)

