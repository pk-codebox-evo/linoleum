package linoleum.mail;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import linoleum.application.Frame;

public class Compose extends Frame {
	private JTextField toField;
	private JTextField ccField;
	private JTextField subField;
	private JTextArea content;
	private final Message msg;
	private final Session session;
	private final int openFrameCount;
	private final Collection<Integer> openFrames;
	private final Preferences prefs = Preferences.userNodeForPackage(getClass());
	private static final int offset = 30;

	public Compose() {
		this(Session.getInstance(System.getProperties()));
	}

	public Compose(final Session session) {
		this(session, new MimeMessage(session));
	}

	public Compose(final Session session, final Message msg) {
		this(session, new HashSet<Integer>(), msg);
	}

	public Compose(final Session session, final Collection<Integer> openFrames) {
		this(session, openFrames, new MimeMessage(session));
	}

	public Compose(final Session session, final Collection<Integer> openFrames, final Message msg) {
		openFrameCount = openFrames.isEmpty()?0:Collections.max(openFrames) + 1;
		openFrames.add(openFrameCount);
		this.openFrames = openFrames;
		this.session = session;
		this.msg = msg;
                setClosable(true);
                setIconifiable(true);
                setMaximizable(true);
                setResizable(true);
		setTitle("Untitled Message " + openFrameCount);
		setIcon(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/ComposeMail24.gif")));

		JPanel top = new JPanel();
		top.setBorder(new EmptyBorder(10, 10, 10, 10));
		top.setLayout(new BorderLayout());
		top.add(buildAddressPanel(), BorderLayout.NORTH);

		content = new JTextArea(15, 30);
		content.setBorder(new EmptyBorder(0, 5, 0, 5));
		content.setLineWrap(true);

		JScrollPane textScroller = new JScrollPane(content);
		top.add(textScroller, BorderLayout.CENTER);

		top.add(buildButtonPanel(), BorderLayout.SOUTH);

		setContentPane(top);
		pack();
		setLocation(offset * openFrameCount, offset * openFrameCount);

		try {
			toField.setText(InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO)));
			ccField.setText(InternetAddress.toString(msg.getRecipients(Message.RecipientType.CC)));
			subField.setText(msg.getSubject());
		} catch (final MessagingException me) {
			me.printStackTrace();
		}
	}

	@Override
	public Frame getFrame() {
		return new Compose(session, openFrames);
	}

	@Override
	public void close() {
		openFrames.remove(openFrameCount);
	}

	private JPanel buildButtonPanel() {
		final JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		final JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				try {
					send();
					try {
						setClosed(true);
					} catch (final PropertyVetoException e) {
						e.printStackTrace();
					}
				} catch (final MessagingException me) {
					me.printStackTrace();
				}
			}
		});
		p.add(send, BorderLayout.EAST);
		return p;
	}

	private void send() throws MessagingException {
		final String from = prefs.get(SimpleClient.name + ".from", null);
		final String to = toField.getText();
		final String cc = ccField.getText();
		final String bcc = null;
		final String subject = subField.getText();
		final String text = content.getText();
		final String file = null;
		final String url = prefs.get(SimpleClient.name + ".url", null);
		final String record = prefs.get(SimpleClient.name + ".record", null);

		if (from != null) {
			msg.setFrom(new InternetAddress(from));
		} else {
			msg.setFrom();
		}
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		if (cc != null) {
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
		}
		if (bcc != null) {
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
		}
		msg.setSubject(subject);
		if (file != null) {
			try {
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText(text);
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.attachFile(file);
				MimeMultipart mp = new MimeMultipart();
				mp.addBodyPart(mbp1);
				mp.addBodyPart(mbp2);
				msg.setContent(mp);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			msg.setText(text);
		}
		msg.setHeader("X-Mailer", SimpleClient.name);
		msg.setSentDate(new Date());
		Transport.send(msg);
		System.out.println("Mail was sent successfully.");

		if (url != null && record != null) {
			final Store store = session.getStore(new URLName(url));
			store.connect();
			final Folder folder = store.getFolder(record);
			if (!folder.exists()) {
				folder.create(Folder.HOLDS_MESSAGES);
			}
			final Message msgs[] = new Message[] {msg};
			folder.appendMessages(msgs);
			System.out.println("Mail was recorded successfully.");
		}
	}

	private JPanel buildAddressPanel() {
		JPanel p = new JPanel();
		p.setLayout(new LabeledPairLayout());

		JLabel toLabel = new JLabel("To: ", JLabel.RIGHT);
		toField = new JTextField(25);
		p.add(toLabel, "label");
		p.add(toField, "field");

		JLabel ccLabel = new JLabel("Cc: ", JLabel.RIGHT);
		ccField = new JTextField(25);
		p.add(ccLabel, "label");
		p.add(ccField, "field");

		JLabel subLabel = new JLabel("Subj: ", JLabel.RIGHT);
		subField = new JTextField(25);
		p.add(subLabel, "label");
		p.add(subField, "field");

		return p;
	}
}
