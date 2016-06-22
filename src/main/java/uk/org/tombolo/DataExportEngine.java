package uk.org.tombolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.tombolo.core.Subject;
import uk.org.tombolo.core.utils.SubjectUtils;
import uk.org.tombolo.execution.spec.DataExportSpecification;
import uk.org.tombolo.execution.spec.DatasourceSpecification;
import uk.org.tombolo.execution.spec.FieldSpecification;
import uk.org.tombolo.execution.spec.SubjectSpecification;
import uk.org.tombolo.exporter.Exporter;
import uk.org.tombolo.field.Field;
import uk.org.tombolo.importer.DownloadUtils;
import uk.org.tombolo.importer.Importer;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class DataExportEngine implements ExecutionEngine{
	private static final Logger log = LoggerFactory.getLogger(DataExportEngine.class);
	private static DownloadUtils downloadUtils;

	DataExportEngine(DownloadUtils downloadUtils) {
		this.downloadUtils = downloadUtils;
	}
	
	public void execute(DataExportSpecification dataExportSpec, Writer writer, boolean forceImport) throws Exception {
		// Import data
		if (forceImport) {
			for (DatasourceSpecification datasourceSpec : dataExportSpec.getDatasetSpecification().getDatasourceSpecification()) {
				log.info("Importing {} {}",
						datasourceSpec.getImporterClass(),
						datasourceSpec.getDatasourceId());
				Importer importer = (Importer) Class.forName(datasourceSpec.getImporterClass()).newInstance();
				importer.setDownloadUtils(downloadUtils);
				int count = importer.importDatasource(datasourceSpec.getDatasourceId());
				log.info("Imported {} values", count);
			}
		}

		// Generate fields
		List<FieldSpecification> fieldSpecs = dataExportSpec.getDatasetSpecification().getFieldSpecification();
		List<Field> fields = new ArrayList<>();
		for (FieldSpecification fieldSpec : fieldSpecs) {
			fields.add(fieldSpec.toField());
		}

		// Use the new fields method
		log.info("Exporting ...");
		List<SubjectSpecification> subjectSpecList = dataExportSpec.getDatasetSpecification().getSubjectSpecification();
		Exporter exporter = (Exporter) Class.forName(dataExportSpec.getExporterClass()).newInstance();
		List<Subject> subjects = new ArrayList<>();
		for (SubjectSpecification subjectSpec : subjectSpecList) {
			subjects.addAll(SubjectUtils.getSubjectBySpecification(subjectSpec));
		}
		exporter.write(writer, subjects, fields);
	}
}
