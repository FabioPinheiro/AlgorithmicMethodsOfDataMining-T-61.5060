d = [0,2,4,6,8,10,12,14];

Task2_data = load('tmp/Task2.csv');


figure('Name','Task 2','NumberTitle','off')
%subplot(2,2,1); plot(d,BF,d,BF2,'--r'); title('Brute Force')
subplot(2,2,2); plot(d,Task2_data(1,:)); title('d-Frequent'); xlim([0 14]);
subplot(2,2,3); plot(d,Task2_data(2,:),'--r'); title('d-Infrequent'); xlim([0 14]);
subplot(2,2,4); plot(d,Task2_data(3,:),'--r'); title('d-Random'); xlim([0 14]);

Task3_data = load('tmp/Task3.csv');

figure('Name','Task 3','NumberTitle','off')
%subplot(2,2,1); plot(d,BF,d,BF2,'--r'); title('Brute Force')
subplot(2,2,2); plot(d,Task3_data(1,:)); title('d-Frequent'); xlim([0 14]);
subplot(2,2,3); plot(d,Task3_data(2,:),'--r'); title('d-Infrequent'); xlim([0 14]);
subplot(2,2,4); plot(d,Task3_data(3,:),'--r'); title('d-Random'); xlim([0 14]);

Task4_data = load('tmp/Task4.csv');

figure('Name','Task 4','NumberTitle','off')
%subplot(2,2,1); plot(d,BF,d,BF2,'--r'); title('Brute Force')
subplot(2,2,2); plot(d,Task4_data(1,:)); title('d-Frequent'); xlim([0 14]);
subplot(2,2,3); plot(d,Task4_data(2,:),'--r'); title('d-Infrequent'); xlim([0 14]);
subplot(2,2,4); plot(d,Task4_data(3,:),'--r'); title('d-Random'); xlim([0 14]);

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
