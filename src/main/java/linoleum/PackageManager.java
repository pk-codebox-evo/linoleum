package linoleum;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.net.URL;
import linoleum.application.event.ClassPathListener;
import linoleum.application.event.ClassPathChangeEvent;
import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.core.retrieve.RetrieveReport;
import org.apache.ivy.core.settings.IvySettings;

public class PackageManager {
	public static final PackageManager instance = new PackageManager();
	private final File lib = new File(System.getProperty("linoleum.home", System.getProperty("user.dir")), "lib");
	private final List<ClassPathListener> listeners = new ArrayList<>();
	private final Map<String, File> map = new HashMap<>();

	private PackageManager() {
		populate();
		add(new File(new File(System.getProperty("java.home")), "../lib/tools.jar"));
		for (final File file: lib.listFiles()) {
			add(file);
		}
	}

	public void addClassPathListener(final ClassPathListener listener) {
		listeners.add(listener);
	}

	public void removeClassPathListener(final ClassPathListener listener) {
		listeners.remove(listener);
	}

	public File getLib() {
		return lib;
	}

	private void populate() {
		final String extdirs[] = System.getProperty("java.ext.dirs").split(File.pathSeparator);
		for (final String str : extdirs) {
			final File dir = new File(str);
			if (dir.isDirectory()) {
				for (final File file : dir.listFiles(new FileFilter() {
					public boolean accept(final File file) {
						return file.isFile() && file.getName().endsWith(".jar");
					}
				})) {
					map.put(new Package(file).getName(), file);
				}
			}
		}
		final String classpath[] = System.getProperty("java.class.path").split(File.pathSeparator);
		for (final String str : classpath) {
			final File file = new File(str);
			if (file.isFile() && file.getName().endsWith(".jar")) {
				map.put(new Package(file).getName(), file);
			}
		}
		if (classpath.length > 0) {
			final File jar = new File(classpath[0]);
			try {
				final URL url = new URL("jar:" + jar.toURI().toURL() + "!/META-INF/MANIFEST.MF");
				final Manifest manifest = new Manifest(url.openStream());
				final String cp = (String)manifest.getMainAttributes().get(Attributes.Name.CLASS_PATH);
				if (cp != null) for (final String str : cp.split(" ")) {
					final File file = new File(jar.getParentFile(), str);
					if (file.isFile() && file.getName().endsWith(".jar")) {
						map.put(new Package(file).getName(), file);
					}
				}
			} catch (final IOException e) {}
		}
	}

	public void install(final String name, final String conf) throws Exception {
		final IvySettings ivySettings = new IvySettings();
		ivySettings.loadDefault();
		final Ivy ivy = Ivy.newInstance(ivySettings);
		final ModuleRevisionId mRID = ModuleRevisionId.parse(name);
		final ResolveOptions resolveOptions = new ResolveOptions();
		resolveOptions.setConfs(new String[]{conf});
		final ResolveReport resolveReport = ivy.resolve(mRID, resolveOptions, true);
		final ModuleDescriptor md = resolveReport.getModuleDescriptor();
		final RetrieveOptions retrieveOptions = new RetrieveOptions();
		retrieveOptions.setDestArtifactPattern(lib.getPath() + "/[artifact]-[revision].[ext]");
		final RetrieveReport retrieveReport = ivy.retrieve(md.getModuleRevisionId(), retrieveOptions);
		for (final Object obj : retrieveReport.getCopiedFiles()) {
			add((File)obj);
		}
		fireClassPathChange(new ClassPathChangeEvent(this));
	}

	private void fireClassPathChange(final ClassPathChangeEvent evt) {
		for (final ClassPathListener listener : listeners) {
			listener.classPathChanged(evt);
		}
	}

	private void add(final File file) {
		final String name = new Package(file).getName();
		if (!map.containsKey(name)) try {
			((ClassLoader)ClassLoader.getSystemClassLoader()).addURL(file.toURI().toURL());
			map.put(name, file);
		} catch (final Exception e) {}
	}
}
