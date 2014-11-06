d = [0,2,4,6,8,10,12,14];


subplot(2,2,1); plot(d,BF); title('Brute Force')
subplot(2,2,2); plot(d,Freq); title('d-Frequent')
subplot(2,2,3); plot(d,InFreq); title('d-Infrequent')
subplot(2,2,4); plot(d,Random); title('d-Random')
