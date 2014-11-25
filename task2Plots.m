d = [0,2,4,6,8,10,12,14];
BF = load('BF.csv');
BF2 = load('BF2.csv');
BF3 = load('BF3.csv');
Freq = load('frequent.csv');
Freq2 = load('frequent2.csv');
Freq3 = load('frequent3.csv');
InFreq = load('infrequent.csv');
InFreq2 = load('infrequent2.csv');
InFreq3 = load('infrequent3.csv');
Random = load('random.csv');
Random2 = load('random2.csv');
Random3 = load('random3.csv');

figure('Name','Comparison Task 2 and Task 3','NumberTitle','off')
subplot(2,2,1); plot(d,BF,d,BF2,'--r'); title('Brute Force')
subplot(2,2,2); plot(d,Freq,d,Freq2,'--r'); title('d-Frequent')
subplot(2,2,3); plot(d,InFreq,d,InFreq2,'--r'); title('d-Infrequent')
subplot(2,2,4); plot(d,Random,d,Random2,'--r'); title('d-Random')



% %%
% figure('Name','Task 3','NumberTitle','off')
% subplot(2,2,1); plot(d,BF2); title('Brute Force')
% subplot(2,2,2); plot(d,Freq2); title('d-Frequent')
% subplot(2,2,3); plot(d,InFreq2); title('d-Infrequent')
% subplot(2,2,4); plot(d,Random2); title('d-Random')
% 
% figure('Name','Task 4','NumberTitle','off')
% subplot(2,2,1); plot(d,BF3); title('Brute Force')
% subplot(2,2,2); plot(d,Freq3); title('d-Frequent')
% subplot(2,2,3); plot(d,InFreq3); title('d-Infrequent')
% subplot(2,2,4); plot(d,Random3); title('d-Random')
