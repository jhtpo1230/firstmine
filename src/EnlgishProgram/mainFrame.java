package EnlgishProgram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class mainFrame extends JFrame {
	JLabel title = new JLabel("토익 공부 화이팅!", SwingConstants.CENTER);
	JPanel fileaddPanel = new JPanel();
	JButton fileaddButton = new JButton("파일 추가하기");
	JButton wordaddButton = new JButton("단어 추가하기");
	JPanel startPanel = new JPanel();
	JPanel testStartPanel = new JPanel();
	JButton studyStart = new JButton("공부 시작히기");
	JButton testStart = new JButton("시험 시작히기");
	JPanel fileSelectPanel = new JPanel();
	JButton select = new JButton("파일 선택하기");
	JLabel fileInformation = new JLabel("선택된 파일 : ");
	int selecting = 0;
	int AddWordNum = 0;
	int TestNum = 1;
	boolean TestFinish = false;

	JTextArea memozang = new JTextArea(20, 10);

	String fileNameString;
	JFileChooser fc = new JFileChooser();;

	class word {
		private String eng;
		private String kor;

		public word(String eng, String kor) {
			this.eng = eng;
			this.kor = kor;
		}

		public String getEng() {
			return eng;
		}

		public String getKor() {
			return kor;
		}
	}

	Vector<word> v = new Vector<word>();

	class wordList {

		public wordList() {
			File file = new File("C:\\Users\\a\\Desktop\\자바2\\영단어 프로그램\\영단어 프로그램 단어장\\" + fileNameString);

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {

				String line;
				int VectorNum = 0;
				ArrayList<String> EngStr = new ArrayList<>();
				ArrayList<String> KorStr = new ArrayList<>();

				while ((line = br.readLine()) != null) {
					String[] splitStr = line.split("\\s+");
					VectorNum++;
					EngStr.add(splitStr[0]);
					KorStr.add(splitStr[1]);
				}

				int m, n;
				int RandomWordArray[] = new int[VectorNum]; // 단어 랜덤 출력을 위한 랜덤 숫자 저장 배열
				Random random = new Random();

				for (m = 0; m < VectorNum; m++) { // 단어가 다 나올 수 있도록 중복 제거
					RandomWordArray[m] = random.nextInt(VectorNum);
					for (n = 0; n < m; n++) {
						if (RandomWordArray[m] == RandomWordArray[n]) {
							m--;
						}
					}
				}
				for (m = 0; m < VectorNum; m++) {
					v.add(new word(EngStr.get(RandomWordArray[m]), KorStr.get(RandomWordArray[m])));
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class finalTest extends JFrame implements ActionListener {
		public int num = 5; // 단어 개수
		public int test = 0; // testSystem 버튼에 따른 변화하는 변수

		String[] data = new String[num]; // 내가 적은 답을 저장할 data 배열을 전역변수로 선언 => 오답노트때 사용하기 위해 전역

		JButton B_1 = new JButton("     채점     ");
		JButton B_2 = new JButton(" 오답노트 ");
		JButton B_3 = new JButton("   재시험   ");

		JButton SaveMemo = new JButton("메모장 저장하기");

		JLabel score = new JLabel();

		JButton backButton = new JButton("돌아가기");
		JButton exitProgram = new JButton("종료하기");

		public finalTest() {
			setTitle("영단어 암기 프로그램");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(400, 800);
			setLayout(new GridLayout(0, 2));

			class WordPanel extends JPanel implements ActionListener {

				JPanel[] OneWordpanel = new JPanel[num];

				wordList WL = new wordList();

				JLabel[] korWord = new JLabel[num];
				JTextField[] engWord = new JTextField[num];

				int i, j;
				int a[] = new int[num]; // 단어 랜덤 출력을 위한 랜덤 숫자 저장 배열
				Random random = new Random();

				public WordPanel() {

					for (i = 0; i < num; i++) {
						OneWordpanel[i] = new JPanel();
						OneWordpanel[i].setLayout(new GridLayout(0, 2));
						OneWordpanel[i].add(korWord[i] = new JLabel(v.get(i).getKor()), BorderLayout.CENTER);
						OneWordpanel[i].add(engWord[i] = new JTextField());
					}
					for (i = 0; i < num; i++) { // 단어가 다 나올 수 있도록 중복 제거
						a[i] = random.nextInt(num);
						for (j = 0; j < i; j++) {
							if (a[i] == a[j]) {
								i--;
							}
						}
					}
					for (i = 0; i < num; i++) {
						this.add(OneWordpanel[a[i]]);
						// OneWordPanel은 30개의 단어 순서대로 저장, 출력만 배열 인덱스 값에 따른 패널 랜덤 출력
					}
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					int correctNum = 0;
					int wrongNum = 0;

					if (e.getSource() == B_1) {
						if (TestFinish == false) {
							int i;

							for (i = 0; i < num; i++) {
								data[i] = engWord[i].getText().strip(); // 내가 적은 답안을 data 배열에 저장

								if (data[i].equalsIgnoreCase((v.get(i)).getEng()) == true) {
									correctNum++;
								}
							}
							wrongNum = num - correctNum;
							String[] wrong = new String[wrongNum]; // 틀린 오답 저장할 wrong 배열
							for (i = 0; i < wrongNum; i++) {
								if (data[i].equalsIgnoreCase((v.get(i)).getEng()) != true) {
									wrong[i] = data[i];
								}
							}

							double result = (double) correctNum / (double) num * 100;
							String RealScore = String.format("%.1f", result);

							score.setText(RealScore + "점");
							TestFinish = true;

							if (result >= 80)
								test = 2; // 합격
							else
								test = 1; // 불합격
						} else if (TestFinish == true) {
							JOptionPane notice = new JOptionPane();
							notice.showMessageDialog(null, "채점이 종료됐으므로 재시험하십시오.");
						}

					} else if (e.getSource() == B_3) { // 재시험을 눌렀지만
						if (TestNum <= 5) {
							TestFinish = false;

							if (test == 0) { // 채점이 안 된 경우
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, "채점을 먼저 진행하십시오.");
							} else if (test == 2) { // 채점 결과가 합격일 경우
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, "합격입니다.");
							} else if (test == 1) { // 채점 결과가 불합격일 경우

								for (i = 0; i < num; i++) {
									this.remove(OneWordpanel[i]);
								}
								num--;

								score.setText("점수");

								for (i = 0; i < num; i++) {
									OneWordpanel[i] = new JPanel();
									OneWordpanel[i].setLayout(new GridLayout(0, 2));
									OneWordpanel[i].add(korWord[i] = new JLabel(v.get(i).getKor()),
											BorderLayout.CENTER);
									OneWordpanel[i].add(engWord[i] = new JTextField());
								}
								for (i = 0; i < num; i++) { // 단어가 다 나올 수 있도록 중복 제거
									a[i] = random.nextInt(num);
									for (j = 0; j < i; j++) {
										if (a[i] == a[j]) {
											i--;
										}
									}
								}
								for (i = 0; i < num; i++) {
									this.add(OneWordpanel[a[i]]);
									// OneWordPanel은 30개의 단어 순서대로 저장, 출력만 배열 인덱스 값에 따른 패널 랜덤 출력
								}
								test = 0;
								TestNum++;
							}
						} else if (TestNum > 4) { // 기회는 5번
							JOptionPane notice = new JOptionPane();
							notice.showMessageDialog(null, "모든 기회가 끝났습니다." + "다시 공부하십시오.");
							dispose();
							new mainFrame();
						}
					}
				}

			} // wordPanel class

			WordPanel WordPanel = new WordPanel();
			WordPanel.setLayout(new GridLayout(0, 1));

			JPanel systemPanel = new JPanel();

			JPanel buttonPanel = new JPanel();
			buttonPanel.setSize(0, 400);
			buttonPanel.setLayout(new GridLayout(3, 1));
			JPanel b_Panel_1 = new JPanel();
			b_Panel_1.add(B_1);
			B_1.addActionListener(WordPanel);
			JPanel b_Panel_2 = new JPanel();
			b_Panel_2.add(B_2);
			B_2.addActionListener(this);
			JPanel b_Panel_3 = new JPanel();
			b_Panel_3.add(B_3);
			B_3.addActionListener(WordPanel);

			buttonPanel.add(b_Panel_1, BorderLayout.CENTER);
			buttonPanel.add(b_Panel_2, BorderLayout.CENTER);
			buttonPanel.add(b_Panel_3, BorderLayout.CENTER);
			systemPanel.add(buttonPanel, BorderLayout.NORTH);

			JPanel memoPanel = new JPanel();
			JLabel memo = new JLabel("메모장");
			memoPanel.add(memo);
			memozang.setLineWrap(true); // 라인 자동 넘기기
			JScrollPane scrollPanel = new JScrollPane(memozang, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 스크롤

			memoPanel.add(scrollPanel);
			systemPanel.add(memoPanel);

			JPanel SaveMemoPanel = new JPanel();
			SaveMemoPanel.add(SaveMemo);
			SaveMemo.addActionListener(this);
			systemPanel.add(SaveMemoPanel);

			JPanel scorePanel = new JPanel();
			score = new JLabel("점수");
			score.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 85));
			scorePanel.add(score);
			systemPanel.add(scorePanel, BorderLayout.SOUTH);

			JPanel conclusionPanel = new JPanel();
			backButton.addActionListener(this);
			exitProgram.addActionListener(this);
			conclusionPanel.add(backButton);
			conclusionPanel.add(exitProgram);
			systemPanel.add(conclusionPanel, BorderLayout.SOUTH);

			add(WordPanel);
			add(systemPanel);

			this.setResizable(false); // JFrame 크기 고정
			this.setLocationRelativeTo(null); // JFrame 위치 가운데 고정

			setVisible(true);
		}

		wrongNote wrongNote;

		class wrongNote extends JDialog {
			JPanel[] WrongNotePanel = new JPanel[num];

			JLabel KorWordLabel = new JLabel();
			JLabel EngWordLabel = new JLabel();

			public wrongNote(String string) {
				JPanel note = new JPanel();

				for (int i = 0; i < num; i++) {
					WrongNotePanel[i] = new JPanel();
					WrongNotePanel[i].setLayout(new GridLayout(0, 3));
					WrongNotePanel[i].add(KorWordLabel = new JLabel(v.get(i).getKor())); // 뜻
					WrongNotePanel[i].add(EngWordLabel = new JLabel(v.get(i).getEng())); // 영단어
					if (data[i].equalsIgnoreCase(v.get(i).getEng()) == true) { // 내 답안이 정답일시
						JLabel MyWordLabel = new JLabel(data[i]); // 내 답안을 적은 라벨
						MyWordLabel.setForeground(Color.BLUE); // 정답은 파랑
						WrongNotePanel[i].add(MyWordLabel);
					} else if (data[i].equalsIgnoreCase(v.get(i).getEng()) == false) { // 내 답안이 오답일시
						JLabel MyWordLabel = new JLabel(data[i]);
						MyWordLabel.setForeground(Color.RED); // 오답은 빨강
						WrongNotePanel[i].add(MyWordLabel);
					}
					note.add(WrongNotePanel[i]);
				}
				note.setLayout(new GridLayout(0, 1));
				this.add(note);
				this.setSize(400, 800);
				this.setLocationRelativeTo(null); // 창 윈도우 가운데
				this.setTitle("오답노트");
				this.setModal(false); // 오답노트를 켜도 메인 창 실행 가능 (부모 프레임 사용 가능)
				this.setVisible(true);
			}
		}

		class MemoAddFrame extends JDialog implements ActionListener {
			JPanel MemoNamePanel = new JPanel();
			JLabel MemoName = new JLabel("저장할 메모장 이름 : ");
			JTextField MemoNameWrite = new JTextField(10);
			JPanel MemoAddPanel = new JPanel();
			JButton MemoAdd = new JButton("저장");

			public MemoAddFrame() {
				this.setTitle("메모장 저장하기");
				MemoNamePanel.add(MemoName);
				MemoNamePanel.add(MemoNameWrite);
				MemoAddPanel.add(MemoAdd);
				MemoAdd.addActionListener(this);
				MemoNamePanel.add(MemoAddPanel);

				getContentPane().add(MemoNamePanel);

				this.setSize(200, 150);
				this.setModal(true);
				this.setVisible(true);

			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == MemoAdd) {
					String MemoNameString = MemoNameWrite.getText();
					FileWriter memoWriter = null;

					try {
						File Memofile = new File(
								"C:\\Users\\a\\Desktop\\자바2\\영단어 프로그램\\영단어 공부 메모장\\" + MemoNameString + ".txt");
						try {
							memoWriter = new FileWriter(
									"C:\\Users\\a\\Desktop\\자바2\\영단어 프로그램\\영단어 공부 메모장\\" + MemoNameString + ".txt",
									true);
							PrintWriter printWriter = new PrintWriter(memoWriter);
							printWriter.print(memozang.getText());

							JOptionPane notice = new JOptionPane();
							notice.showMessageDialog(null, "메모장 저장 완료: " + MemoNameString + ".txt");

							printWriter.close();

						} catch (Exception e1) {
							e1.getStackTrace();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					dispose();
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == B_2) { // 오답노트를 눌렀지만
				if (test == 0) { // 채점이 안 된 경우
					JOptionPane notice = new JOptionPane();
					notice.showMessageDialog(null, "채점을 먼저 진행하십시오.");
				} else {
					wrongNote = new wrongNote(e.getActionCommand());
				}
			} else if (e.getSource() == B_3) { // 재시험을 눌렀지만
				if (test == 0) { // 채점이 안 된 경우
					JOptionPane notice = new JOptionPane();
					notice.showMessageDialog(null, "채점을 먼저 진행하십시오.");
				} else if (test == 2) { // 채점 결과가 합격일 경우
					JOptionPane notice = new JOptionPane();
					notice.showMessageDialog(null, "합격입니다.");
				}
			} else if (e.getSource() == SaveMemo) {
				if ((memozang.getText()).equals(null)) {
					JOptionPane notice = new JOptionPane();
					notice.showMessageDialog(null, "메모장에 저장할 내용을 입력하시오");
				} else {
					new MemoAddFrame();
				}
			} else if (e.getSource() == backButton) {
				this.dispose();
				new mainFrame();
			} else if (e.getSource() == exitProgram) {
				JOptionPane notice = new JOptionPane();
				notice.showMessageDialog(null, "고생하셨습니다");
				this.dispose();
			}
		}
	}

	int studyTime = 1;

	class studyNote extends JDialog { // 단어가 많아지니 스크롤을 사용할 것

		JLabel KorWordStudyLabel;
		JLabel EngWordStudyLabel;

		public studyNote() throws IOException {

			if (studyTime == 1) {
				new wordList();
			}

			JPanel studyNoteWholePanel = new JPanel();
			JPanel[] studyNotePanel = new JPanel[(v.size()) + AddWordNum];
			System.out.println(v.size());
			System.out.println(studyTime);
			System.out.println(AddWordNum);

			studyNoteWholePanel.setLayout(new GridLayout(0, 1));

			File fileForStudy = new File("C:\\Users\\a\\Desktop\\자바2\\영단어 프로그램\\영단어 프로그램 단어장\\" + fileNameString);
			ArrayList<String> EngStr = new ArrayList<>();
			ArrayList<String> KorStr = new ArrayList<>();

			try (BufferedReader studyBr = new BufferedReader(new FileReader(fileForStudy))) {

				String line;

				while ((line = studyBr.readLine()) != null) {
					String[] splitStr = line.split("\\s+");
					EngStr.add(splitStr[0]);
					KorStr.add(splitStr[1]);
				}
			}

			for (int i = 0; i < (v.size() + AddWordNum); i++) {
				studyNotePanel[i] = new JPanel();
				studyNotePanel[i].setLayout(new GridLayout(0, 2));
				studyNotePanel[i].add(KorWordStudyLabel = new JLabel(KorStr.get(i))); // 뜻
				studyNotePanel[i].add(EngWordStudyLabel = new JLabel(EngStr.get(i))); // 영단어
				EngWordStudyLabel.setForeground(Color.BLUE);
				studyNoteWholePanel.add(studyNotePanel[i]);
			}

			this.add(studyNoteWholePanel);
			this.setSize(400, 800);
			this.setLocationRelativeTo(null); // 창 윈도우 가운데
			this.setTitle("공부노트");
			this.setModal(false); // 공부노트를 켜도 메인 창 실행 가능 (부모 프레임 사용 가능)
			this.setVisible(true);
		}
	}

	public mainFrame() {
		setTitle("영단어 암기 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);

		class myPanel extends JPanel implements ActionListener {

			public myPanel() {
				setLayout(new GridLayout(2, 0));
				add(title);
				title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
				startPanel.setLayout(new GridLayout(4, 0));
				fileaddPanel.add(fileaddButton);
				fileaddButton.addActionListener(this);
				fileaddPanel.add(wordaddButton);
				wordaddButton.addActionListener(this);
				startPanel.add(fileaddPanel, BorderLayout.CENTER);
				fileSelectPanel.add(select);
				startPanel.add(fileSelectPanel, BorderLayout.CENTER);
				select.addActionListener(this);
				fileInformation.setOpaque(true);
				fileInformation.setBackground(Color.WHITE);
				startPanel.add(fileInformation);
				testStartPanel.add(studyStart);
				testStartPanel.add(testStart);
				startPanel.add(testStartPanel, BorderLayout.CENTER);
				studyStart.addActionListener(this);
				testStart.addActionListener(this);
				add(startPanel);

			}

			class fileAddFrame extends JDialog implements ActionListener {
				JPanel fileNamePanel = new JPanel();
				JLabel fileName = new JLabel("생성할 파일 이름 : ");
				JTextField fileNameWrite = new JTextField(10);
				JPanel fileAddPanel = new JPanel();
				JButton fileAdd = new JButton("생성");

				public fileAddFrame() {
					this.setTitle("영단어 파일 추가하기");
					fileNamePanel.add(fileName);
					fileNamePanel.add(fileNameWrite);
					fileAddPanel.add(fileAdd);
					fileAdd.addActionListener(this);
					fileNamePanel.add(fileAddPanel);

					getContentPane().add(fileNamePanel);

					this.setSize(200, 150);
					this.setModal(true);
					this.setVisible(true);

				}

				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == fileAdd) {
						fileNameString = fileNameWrite.getText();
						// 파일 생성
						try {
							// 파일 객체 생성
							File file = new File(
									"C:\\Users\\a\\Desktop\\자바2\\영단어 프로그램\\영단어 프로그램 단어장\\" + fileNameString + ".txt");
							if (file.createNewFile()) {
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, "파일 생성 완료: " + file.getName());
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						dispose();
					}
				}
			}

			class wordAddFrame extends JDialog implements ActionListener {

				JLabel fileInfor = new JLabel("null");

				JPanel WordAddButtonPanel = new JPanel();
				JButton WordAdd = new JButton("추가");

				JPanel WordAddSystemPanel = new JPanel();

				JPanel EngAddPanel = new JPanel();
				JLabel EngAddLabel = new JLabel("영단어 : ");
				JTextField EngAddField = new JTextField(15);

				JPanel KorAddPanel = new JPanel();
				JLabel KorAddLabel = new JLabel("뜻 : ");
				JTextField KorAddField = new JTextField(15);

				JPanel WordAddWholePanel = new JPanel();

				public wordAddFrame() {
					this.setTitle("영단어 추가하기");
					WordAddWholePanel.setLayout(new GridLayout(3, 0));
					WordAddWholePanel.add(fileInfor);
					if (selecting == 0) {
						JOptionPane notice = new JOptionPane();
						notice.showMessageDialog(null, "파일을 선택하십시오.");
					} else if (selecting == 1) {
						fileInfor.setText(fileInformation.getText());
						WordAddSystemPanel.setLayout(new GridLayout(2, 0));

						EngAddPanel.add(EngAddLabel);
						EngAddPanel.add(EngAddField);
						WordAddSystemPanel.add(EngAddPanel);

						KorAddPanel.add(KorAddLabel);
						KorAddPanel.add(KorAddField);
						WordAddSystemPanel.add(KorAddPanel);

						WordAddWholePanel.add(WordAddSystemPanel);
						WordAddButtonPanel.add(WordAdd);
						WordAdd.addActionListener(this);
						WordAddWholePanel.add(WordAddButtonPanel);

						getContentPane().add(WordAddWholePanel);

						this.setSize(400, 200);
						this.setModal(true);
						this.setVisible(true);
					}
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					// 단어 추가
					if (e.getSource() == WordAdd) {
						// 파일 위치 : fileInformation
						// 영어 : EngAddField , 뜻 : KorAddField
						FileWriter fileWriter = null;

						if (Pattern.matches("^[ㄱ-ㅎ가-힣]*$", KorAddField.getText()) != true
								|| Pattern.matches("^[a-zA-Z]*$", EngAddField.getText()) != true) {
							if (Pattern.matches("^[ㄱ-ㅎ가-힣]*$", KorAddField.getText()) == false
									&& Pattern.matches("^[a-zA-Z]*$", EngAddField.getText()) == false) {
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, "영단어를 입력할시 영어만, " + "\n" + "뜻을 입력할시 한글만 입력하시오.");
							} else if (Pattern.matches("^[ㄱ-ㅎ가-힣]*$", KorAddField.getText()) == false) {
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, "뜻을 입력할시 한글만 입력하시오.");
							} else if (Pattern.matches("^[a-zA-Z]*$", EngAddField.getText()) == false) {
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, "영단어를 입력할시 영어만 입력하시오.");
							}
						} else {
							try {
								fileWriter = new FileWriter(
										"C:\\Users\\a\\Desktop\\자바2\\영단어 프로그램\\영단어 프로그램 단어장\\" + fileNameString, true);
								PrintWriter printWriter = new PrintWriter(fileWriter);
								printWriter.print(EngAddField.getText() + " ");
								printWriter.print(KorAddField.getText());
								printWriter.print("\n");
								JOptionPane notice = new JOptionPane();
								notice.showMessageDialog(null, EngAddField.getText() + "가 추가되었습니다.");
								AddWordNum++;
								EngAddField.setText(null);
								KorAddField.setText(null);
								printWriter.flush();

							} catch (Exception e1) {
								e1.getStackTrace();
							}
						}
					}

				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == fileaddButton) {
					new fileAddFrame();

				} else if (e.getSource() == select) {
					int returnVal = fc.showOpenDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							fileInformation.setText("선택된 파일 : " + file.getName());
							fileNameString = file.getName();
							selecting = 1;
						} catch (Exception e1) {

						}
					}
				} else if (e.getSource() == wordaddButton) {
					new wordAddFrame();
				} else if (e.getSource() == studyStart) {
					if (selecting == 0) {
						JOptionPane notice = new JOptionPane();
						notice.showMessageDialog(null, "파일을 선택하십시오.");
					} else if (selecting == 1) {
						try {
							new studyNote();
							studyTime++;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				else if (e.getSource() == testStart) {
					if (selecting == 0) {
						JOptionPane notice = new JOptionPane();
						notice.showMessageDialog(null, "파일을 선택하십시오.");
					} else if (selecting == 1) {
						dispose(); // 창 없애기
						new finalTest();
					}
				}
			}
		}

		JPanel myPanel = new myPanel();

		this.add(myPanel);
		this.setResizable(false); // JFrame 크기 고정
		setVisible(true);
	}

	public static void main(String[] args) {
		new mainFrame();
	}
}
