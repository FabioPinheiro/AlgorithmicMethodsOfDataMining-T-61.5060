sortedList = importdata('tmp/TermsSortedByNumberOfTweets.csv');

figure(1)
hist(sortedList.data,250)

figure(2)
%Cummulative distribuition

figure(3)
axis = 1:length(sortedList.data);
loglog(axis,sortedList.data)

