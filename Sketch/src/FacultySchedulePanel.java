

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.pack_scheduler.catalog.CourseCatalog;
import edu.ncsu.csc216.pack_scheduler.course.Course;
import edu.ncsu.csc216.pack_scheduler.manager.RegistrationManager;
import edu.ncsu.csc216.pack_scheduler.user.Faculty;
import edu.ncsu.csc216.pack_scheduler.user.Student;
import edu.ncsu.csc216.pack_scheduler.user.schedule.FacultySchedule;
import edu.ncsu.csc216.pack_scheduler.user.schedule.Schedule;

/**
 * Creates a user interface for students to register for classes.
 * 
 * @author Sarah Heckman
 */
public class FacultySchedulePanel  extends JPanel implements ActionListener {
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** JTable for displaying the catalog of Courses */
	private JTable tableCatalog;
	/** JTable for displaying the schedule of Courses */
	private JTable tableSchedule;
	/** TableModel for catalog */
	private CourseTableModel catalogTableModel;
	
	/** Course roll model */
	private CourseRollModel courseRoll;
//	/** TableModel for schedule */
//	private CourseTableModel scheduleTableModel;
	/** Student's Schedule title label */
	private JLabel lblScheduleTitle;
	/** Student's Schedule text field */
	private JTextField txtScheduleTitle;

	/** Scroll for student schedule */
	private JScrollPane scrollSchedule;
	/** Border for Schedule */
	private TitledBorder borderSchedule;
	/** Panel for displaying Course Details */
	private JPanel pnlCourseDetails;
	/** Label for Course Details name title */
	private JLabel lblNameTitle = new JLabel("Name: ");
	/** Label for Course Details section title */
	private JLabel lblSectionTitle = new JLabel("Section: ");
	/** Label for Course Details title title */
	private JLabel lblTitleTitle = new JLabel("Title: ");
	/** Label for Course Details instructor title */
	private JLabel lblInstructorTitle = new JLabel("Instructor: ");
	/** Label for Course Details credit hours title */
	private JLabel lblCreditsTitle = new JLabel("Credits: ");
	/** Label for Course Details meeting title */
	private JLabel lblMeetingTitle = new JLabel("Meeting: ");
	/** Label for Course Details enrollment cap title */
	private JLabel lblEnrollmentCapTitle = new JLabel("Enrollment Cap: ");
	/** Label for Course Details open seats title */
	private JLabel lblOpenSeatsTitle = new JLabel("Open Seats: ");
	/** Label for Course Details name */
	private JLabel lblName = new JLabel("");
	/** Label for Course Details section */
	private JLabel lblSection = new JLabel("");
	/** Label for Course Details title */
	private JLabel lblTitle = new JLabel("");
	/** Label for Course Details instructor */
	private JLabel lblInstructor = new JLabel("");
	/** Label for Course Details credit hours */
	private JLabel lblCredits = new JLabel("");
	/** Label for Course Details meeting */
	private JLabel lblMeeting = new JLabel("");
	/** Label for Course Details enrollment cap */
	private JLabel lblEnrollmentCap = new JLabel("");
	/** Label for Course Details open seats */
	private JLabel lblOpenSeats = new JLabel("");
	/** Current user */
	private Faculty currentUser;
	/** Course catalog */
	private CourseCatalog catalog;
	/** Current user's schedule */
	private FacultySchedule schedule;
	
	
	/**
	 * Creates the requirements list.
	 */
	public FacultySchedulePanel() {
		super(new GridBagLayout());
		
		RegistrationManager manager = RegistrationManager.getInstance();
		currentUser = (Faculty)manager.getCurrentUser();
		catalog = manager.getCourseCatalog();
		

//		lblScheduleTitle = new JLabel("Schedule Title: ");
//		txtScheduleTitle = new JTextField("", 20); 
//
//		
//		JPanel pnlActions = new JPanel();
//		pnlActions.setLayout(new GridLayout(3, 1));
//		JPanel pnlAddRemove = new JPanel();
//		pnlAddRemove.setLayout(new GridLayout(1, 2));
//
//		JPanel pnlResetDisplay = new JPanel();
//		pnlResetDisplay.setLayout(new GridLayout(1, 2));
//
//		JPanel pnlScheduleTitle = new JPanel();
//		pnlScheduleTitle.setLayout(new GridLayout(1, 3));
//		pnlScheduleTitle.add(lblScheduleTitle);
//		pnlScheduleTitle.add(txtScheduleTitle);
//
//		pnlActions.add(pnlAddRemove);
//		pnlActions.add(pnlResetDisplay);
//		pnlActions.add(pnlScheduleTitle);
//		
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//		TitledBorder borderActions = BorderFactory.createTitledBorder(lowerEtched, "Actions");
//		pnlActions.setBorder(borderActions);
//		pnlActions.setToolTipText("Scheduler Actions");
//					
		//Set up Catalog table
		catalogTableModel = new CourseTableModel(true);
		tableCatalog = new JTable(catalogTableModel) {
			private static final long serialVersionUID = 1L;
			
			/**
			 * Set custom tool tips for cells
			 * @param e MouseEvent that causes the tool tip
			 * @return tool tip text
			 */
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				int realColumnIndex = convertColumnIndexToModel(colIndex);
				
				return (String)catalogTableModel.getValueAt(rowIndex, realColumnIndex);
			}
		};
		tableCatalog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCatalog.setPreferredScrollableViewportSize(new Dimension(500, 500));
		tableCatalog.setFillsViewportHeight(true);
		tableCatalog.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				String name = tableCatalog.getValueAt(tableCatalog.getSelectedRow(), 0).toString();
				String section = tableCatalog.getValueAt(tableCatalog.getSelectedRow(), 1).toString();
				Course c = catalog.getCourseFromCatalog(name, section);
				updateCourseDetails(c);
			}
			
		});
		
		JScrollPane scrollCatalog = new JScrollPane(tableCatalog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		TitledBorder borderCatalog = BorderFactory.createTitledBorder(lowerEtched, "Faculty Schedule");
		scrollCatalog.setBorder(borderCatalog);
		scrollCatalog.setToolTipText("Course Catalog");
		
		//Set up the course details panel
		pnlCourseDetails = new JPanel();
		pnlCourseDetails.setLayout(new GridLayout(5, 1));
		JPanel pnlName = new JPanel(new GridLayout(1, 4));
		pnlName.add(lblNameTitle);
		pnlName.add(lblName);
		pnlName.add(lblSectionTitle);
		pnlName.add(lblSection);
		
		JPanel pnlTitle = new JPanel(new GridLayout(1, 1));
		pnlTitle.add(lblTitleTitle);
		pnlTitle.add(lblTitle);
		
		JPanel pnlInstructor = new JPanel(new GridLayout(1, 4));
		pnlInstructor.add(lblInstructorTitle);
		pnlInstructor.add(lblInstructor);
		pnlInstructor.add(lblCreditsTitle);
		pnlInstructor.add(lblCredits);
		
		JPanel pnlMeeting = new JPanel(new GridLayout(1, 1));
		pnlMeeting.add(lblMeetingTitle);
		pnlMeeting.add(lblMeeting);
		
		JPanel pnlEnrollment = new JPanel(new GridLayout(1, 4));
		pnlEnrollment.add(lblEnrollmentCapTitle);
		pnlEnrollment.add(lblEnrollmentCap);
		pnlEnrollment.add(lblOpenSeatsTitle);
		pnlEnrollment.add(lblOpenSeats);
		
		pnlCourseDetails.add(pnlName);
		pnlCourseDetails.add(pnlTitle);
		pnlCourseDetails.add(pnlInstructor);
		pnlCourseDetails.add(pnlMeeting);
		pnlCourseDetails.add(pnlEnrollment);
		
		TitledBorder borderCourseDetails = BorderFactory.createTitledBorder(lowerEtched, "Course Details");
		pnlCourseDetails.setBorder(borderCourseDetails);
		pnlCourseDetails.setToolTipText("Course Details");
		
		//Set up Schedule table
		courseRoll = new CourseRollModel(false);
		tableSchedule = new JTable(courseRoll);
		tableSchedule.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSchedule.setPreferredScrollableViewportSize(new Dimension(500, 500));
		tableSchedule.setFillsViewportHeight(true);
		
		scrollSchedule = new JScrollPane(tableSchedule, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		borderSchedule = BorderFactory.createTitledBorder(lowerEtched, "");
		scrollSchedule.setBorder(borderSchedule);
						
		updateTables();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		add(scrollCatalog, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
//		add(pnlActions, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		add(pnlCourseDetails, c);
		

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		add(scrollSchedule, c);
		
	}

	/**
	 * Performs an action based on the given {@link ActionEvent}.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {

		
	
	}
	
	/**
	 * Updates the catalog and schedule tables.
	 */
	public void updateTables() {
		catalogTableModel.updateData();
		courseRoll.updateData();
	}
	
	/**
	 * Updates the pnlCourseDetails with full information about the most
	 * recently selected course.
	 */
	private void updateCourseDetails(Course c) {
		if (c != null) {
			lblName.setText(c.getName());
			lblSection.setText(c.getSection());
			lblTitle.setText(c.getTitle());
			lblInstructor.setText(c.getInstructorId());
			lblCredits.setText("" + c.getCredits());
			lblMeeting.setText(c.getMeetingString());
			lblEnrollmentCap.setText("" + c.getCourseRoll().getEnrollmentCap());
			lblOpenSeats.setText("" + c.getCourseRoll().getOpenSeats());
		}
	}
	
	/**
	 * {@link CourseTableModel} is the object underlying the {@link JTable} object that displays
	 * the list of {@link Course}s to the user.
	 * @author Sarah Heckman
	 */
	private class CourseTableModel extends AbstractTableModel {
		
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Column names for the table */
		private String [] columnNames = {"Name", "Section", "Title", "Meeting Days", "Open Seats"};
		/** Data stored in the table */
		private Object [][] data;
		/** Boolean flag if the model applies to the catalog or schedule */
		private boolean isCatalog;
		
		/**
		 * Constructs the {@link CourseTableModel} by requesting the latest information
		 * from the {@link RequirementTrackerModel}.
		 */
		public CourseTableModel(boolean isCatalog) {
			this.isCatalog = isCatalog;
			updateData();
		}

		/**
		 * Returns the number of columns in the table.
		 * @return the number of columns in the table.
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * Returns the number of rows in the table.
		 * @return the number of rows in the table.
		 */
		public int getRowCount() {
			if (data == null) 
				return 0;
			return data.length;
		}
		
		/**
		 * Returns the column name at the given index.
		 * @return the column name at the given column.
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		 * Returns the data at the given {row, col} index.
		 * @return the data at the given location.
		 */
		public Object getValueAt(int row, int col) {
			if (data == null)
				return null;
			return data[row][col];
		}
		
		/**
		 * Sets the given value to the given {row, col} location.
		 * @param value Object to modify in the data.
		 * @param row location to modify the data.
		 * @param column location to modify the data.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
		
		/**
		 * Updates the given model with {@link Course} information from the {@link WolfScheduler}.
		 */
		private void updateData() {
			if (isCatalog) {
				data = catalog.getCourseCatalog();
			} else {
				currentUser = (Faculty)RegistrationManager.getInstance().getCurrentUser();
				if (currentUser != null) {
					schedule = currentUser.getSchedule();
					txtScheduleTitle.setText("CourseRoll");
//					borderSchedule.setTitle(schedule.getTitle());
//					scrollSchedule.setToolTipText(schedule.getTitle());
					data = schedule.getScheduledCourses();
					
					FacultySchedulePanel.this.repaint();
					FacultySchedulePanel.this.validate();
				}
			}
		}
	}
	
	private class CourseRollModel extends AbstractTableModel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Column names for the table */
		private String [] columnNames = {"First Name", "Last Name", "StudentID"};
		/** Data stored in the table */
		private Object [][] data;
		/** Boolean flag if the model applies to the catalog or schedule */
		private boolean isCatalog;
		
		/**
		 * Constructs the {@link CourseTableModel} by requesting the latest information
		 * from the {@link RequirementTrackerModel}.
		 */
		public CourseRollModel(boolean isCatalog) {
			this.isCatalog = isCatalog;
			updateData();
		}

		/**
		 * Returns the number of columns in the table.
		 * @return the number of columns in the table.
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * Returns the number of rows in the table.
		 * @return the number of rows in the table.
		 */
		public int getRowCount() {
			if (data == null) 
				return 0;
			return data.length;
		}
		
		/**
		 * Returns the column name at the given index.
		 * @return the column name at the given column.
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		 * Returns the data at the given {row, col} index.
		 * @return the data at the given location.
		 */
		public Object getValueAt(int row, int col) {
			if (data == null)
				return null;
			return data[row][col];
		}
		
		/**
		 * Sets the given value to the given {row, col} location.
		 * @param value Object to modify in the data.
		 * @param row location to modify the data.
		 * @param column location to modify the data.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
		
		/**
		 * Updates the given model with {@link Course} information from the {@link WolfScheduler}.
		 */
		private void updateData() {
			if (isCatalog) {
				data = catalog.getCourseCatalog();
			} else {
				currentUser = (Faculty)RegistrationManager.getInstance().getCurrentUser();
				if (currentUser != null) {
					schedule = currentUser.getSchedule();
					txtScheduleTitle.setText("CourseRoll");
//					borderSchedule.setTitle(schedule.getTitle());
//					scrollSchedule.setToolTipText(schedule.getTitle());
					data = schedule.getScheduledCourses();
					
					FacultySchedulePanel.this.repaint();
					FacultySchedulePanel.this.validate();
				}
			}
		}
	}
	

}