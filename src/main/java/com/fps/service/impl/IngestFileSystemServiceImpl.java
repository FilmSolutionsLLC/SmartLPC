package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.IngestServer;
import com.fps.domain.Ingests;
import com.fps.domain.Lookups;
import com.fps.domain.User;
import com.fps.elastics.search.IngestsSearchRepository;
import com.fps.repository.*;
import com.fps.security.SecurityUtils;
import com.fps.service.IngestFileSystemService;
import com.fps.service.util.ShellCommandRunner;
import com.fps.web.rest.dto.IngestFileSystem;
import com.fps.web.rest.dto.ProjectsIngestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by macbookpro on 2/1/17.
 */
@Service
public class IngestFileSystemServiceImpl implements IngestFileSystemService {

	private final Logger log = LoggerFactory.getLogger(IngestFileSystemServiceImpl.class);

	@Inject
	private ShellCommandRunner shellCommandRunner;

	@Inject
	private ProjectsRepository projectsRepository;

	@Inject
	private IngestsRepository ingestsRepository;

	@Inject
	private IngestsSearchRepository ingestsSearchRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

	@Inject
	private LookupsRepository lookupsRepository;

	@Inject
	private IngestServerRepository ingestServerRepository;

	@Inject
	private JdbcTemplate jdbcTemplate;

	/**
	 * New Ingests return all ingests to view
	 *
	 * @return
	 */
	public Set<IngestFileSystem> newIngests() {

		Set<IngestFileSystem> allNewIngest = new HashSet<>();
		// final List<Lookups> scratchServers =
		// lookupsRepository.findByTableNameAndFieldName(Constants.CONFIG,
		// Constants.SCRATCH_SERVER);

		// Scan Scratch Server for to be ingested projects
		final Lookups sourceServer = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.INGEST_SERVER);
		final Lookups sourcePath = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.INGEST_NEW);

		final String source = sourceServer.getTextValue();
		final Lookups ingest_new = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.INGEST_NEW);
		final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);
		final String sshCommand = "ssh " + username.getTextValue() + "@" + source;
		final String baseCommand = sshCommand.concat(" ls " + ingest_new.getTextValue());

		// Get all projects from particular server
		final List<String> projectsToIngest = shellCommandRunner.getFilesAndFolders(baseCommand);

		// Loop Projects
		for (String project : projectsToIngest) {

			// Get Title of project
			final String commandForTitle2 = baseCommand.concat(project);
			final String alfrescoTitle2 = shellCommandRunner.executeCommand(commandForTitle2);

			// check if title 2 doesn't exists which means nothing to ingest
			if (alfrescoTitle2 != null && !alfrescoTitle2.isEmpty()) {

				final String TITLE_1 = project;
				final String TITLE_2 = alfrescoTitle2.trim();

				currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);

				// Check and Get project from database for project to ingest
				final String getProjectSQL = "select id,name,images_location,images_location_remote from projects where alfresco_title_1='"
						+ TITLE_1 + "' and alfresco_title_2='" + TITLE_2 + "'";

				ProjectsIngestDTO projects = null;
				try {
					projects = jdbcTemplate.queryForObject(getProjectSQL, new Object[] {},
							new BeanPropertyRowMapper<>(ProjectsIngestDTO.class));

					final String totalImageCountCommand = sshCommand.concat(" find ")
							.concat(ingest_new.getTextValue().concat(TITLE_1).concat("/").concat(TITLE_2)
									.concat(Constants.NEW).concat(" -type f -print | wc -l"));

					final String imageCountOp = shellCommandRunner.executeCommand(totalImageCountCommand);

					final String totalImages = imageCountOp.trim();

					// 0 images to be ingest hence throw warning
					if (totalImages.equals("0")) {
						IngestFileSystem ingestFileSystem = new IngestFileSystem(null,
								TITLE_1.concat("/").concat(TITLE_2), false, Constants.WARNING, "0",
								"No files present to be ingested", null, source, null, null, null);
						allNewIngest.add(ingestFileSystem);
					}
					// Images found
					// Check every sub folder for image count
					// If mismatch then throw warnings
					// Display image count related to each folder
					else {

						IngestFileSystem ingestFileSystem = new IngestFileSystem();

						Ingests existsInDB = ingestsRepository.findByAlfrescoTitle1AndAlfrescoTitle2AndStatus(TITLE_1,
								TITLE_2, Constants.NOTSTARTED);

						// check if entry already exists in db
						if (existsInDB != null) {
							// if exists in db and it had been started , don't
							// display that project
							if (existsInDB.getStatus().equals(Constants.NOTSTARTED)) {
								final String params[] = { sshCommand, (ingest_new.getTextValue()).concat(TITLE_1)
										.concat("/").concat(TITLE_2).concat(Constants.NEW) };
								List<String> individualFolderCount = eachFolderImageCount(params);
								final String warning = individualFolderCount.get(individualFolderCount.size() - 1);

								individualFolderCount.remove(individualFolderCount.size() - 1);

								ingestFileSystem = new IngestFileSystem(existsInDB.getId(),
										project.concat("/").concat(alfrescoTitle2).trim(), true, Constants.NOTSTARTED,
										totalImages, warning, individualFolderCount, source, null, null, null);
								allNewIngest.add(ingestFileSystem);
							} else {
								continue;
								// skip the projects which have been started
							}
						} else {

							final String params[] = { sshCommand, (ingest_new.getTextValue()).concat(TITLE_1)
									.concat("/").concat(TITLE_2).concat(Constants.NEW) };

							List<String> individualFolderCount = eachFolderImageCount(params);

							// get warning set in list from end of the list
							final String warning = individualFolderCount.get(individualFolderCount.size() - 1);

							// Remove that warning from end of the list
							individualFolderCount.remove(individualFolderCount.size() - 1);

							Ingests ingests = new Ingests();
							ingests.setProject_id(projects.getId());
							ingests.setAlfrescoTitle1(TITLE_1);
							ingests.setAlfrescoTitle2(TITLE_2);
							ingests.setTotalImages(Integer.valueOf(totalImages));
							ingests.setStatus(Constants.NOTSTARTED);
							ingests.setSourceServer(source);
							ingests.setSourcePath(sourcePath.getTextValue());

							// save ingest in DB
							Ingests currentSaved = ingestsRepository.save(ingests);

							ingestFileSystem = new IngestFileSystem(currentSaved.getId(),
									TITLE_1.concat("/").concat(TITLE_2), true, Constants.NOTSTARTED, totalImages,
									warning, individualFolderCount, source, null, null, null);
							// add ingest to view
							allNewIngest.add(ingestFileSystem);

						}
					}

				} catch (EmptyResultDataAccessException e) {
					projects = null;
					IngestFileSystem ingestFileSystem = new IngestFileSystem(null, TITLE_1.concat("/").concat(TITLE_2),
							false, Constants.ERROR, "0", "Check folder Structure & Spell Checks", null, source, null,
							null, null);
					// add ingest to view
					allNewIngest.add(ingestFileSystem);
				}

			} else {
				// if no at2..no images to be ingested at all
				System.out.println("No Title 2 found");
			}
		}

		return allNewIngest;
	}
	// find /mnt/ingest/new/Charlie_Banks_Proj/Charlie_Banks -type f -print | wc
	// -l

	/**
	 * Start Ingest
	 *
	 * @param ingestFileSystem
	 * @return
	 */
	public Boolean startIngest(IngestFileSystem ingestFileSystem) {

		currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);

		final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		Ingests ingest = ingestsRepository.findOne(ingestFileSystem.getIngestID());
		ingest.setAdminOwner(user);
		ingest.setIngestStartTime(ZonedDateTime.now());

		final String action = ingestFileSystem.getAction();
		switch (action) {
		case Constants.RUN_INGEST_ONLY:
			ingest.setAction(0);
			break;
		case Constants.CONVERT_FROM_ZOOMS:
			ingest.setAction(1);
			break;
		case Constants.CONVERT_FROM_SUPERZOOMS:
			ingest.setAction(2);
			break;
		case Constants.CONVERT_FROM_MASTERS:
			ingest.setAction(3);
			break;
		}

		final String priority = ingestFileSystem.getPriority();
		switch (priority) {
		case Constants.HIGH:
			ingest.setPriority(0);
			break;
		case Constants.NORMAL:
			ingest.setPriority(1);
			break;
		case Constants.LOW:
			ingest.setPriority(2);
			break;
		}
		// check for
		if (ingestFileSystem.getLogo().equals("NONE")) {
			ingest.setWatermark(false);
			ingest.setWmFile("");
		} else {
			ingest.setWatermark(true);
			ingest.setWmFile(ingestFileSystem.getLogo());
		}

		final Lookups ingestTouchFileServer = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.INGEST_SERVER);
		final Lookups ingestTouchFileLocation = lookupsRepository.findByConfigAndKey(Constants.CONFIG,
				Constants.INGEST_QUEUE);
		final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);

		final String tokenFile = ingest.getPriority() + (".").concat(String.valueOf(ingest.getId()));

		// final String sshCommand = "ssh " + username.getTextValue() + "@" +
		// ingestScriptServer.getTextValue() + " " +
		// ingestScriptLocation.getTextValue() + " " +
		// ingestFileSystem.getIngestID();

		// log.info("ssh command : " + sshCommand);

		final String createFileCommand = "ssh " + username.getTextValue() + "@" + ingestTouchFileServer.getTextValue()
				+ " touch -a " + ingestTouchFileLocation.getTextValue() + "/" + tokenFile;
		log.info("createFileCommand command : " + createFileCommand);
		try {
			final Integer fileCreated = shellCommandRunner.executeForStatusCode(createFileCommand);
			if (fileCreated == 0) {
				// file created
				ingest.setStatus(Constants.QUEUED);
				currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
				ingestsRepository.save(ingest);
				return true;
			} else {
				ingest.setStatus(Constants.FAILED);
				ingest.setIngestCompletedTime(ZonedDateTime.now());
				currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
				ingestsRepository.save(ingest);
				return false;
			}
		} catch (Exception e) {

			ingest.setStatus(Constants.FAILED);
			ingest.setIngestCompletedTime(ZonedDateTime.now());
			currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
			ingestsRepository.save(ingest);
			e.printStackTrace();
			return false;
		}
	}

	// ssh $ingestprocessorIP kill -TSTP $PID_OF_PROCESS
	/**
	 * 
	 */
	public void pause(Ingests ingests) {
		ingests.setStatus(Constants.PAUSED);
		final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);

		String pauseCommand = "ssh " + username.getTextValue() + "@" + ingests.getIngestProcessor() + " kill -TSTP "
				+ ingests.getId();
		log.info("PAUSE COMMAND : " + pauseCommand);
		final Integer paused = shellCommandRunner.executeForStatusCode(pauseCommand);
		if (paused == 0) {
			currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
			ingestsRepository.save(ingests);
		}
	}

	// ssh $ingestprocessor kill -CONT $PID_OF_PROCESS
	public void resume(Ingests ingests) {

		ingests.setStatus(Constants.QUEUED);
		final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);
		String resumeCommand = "ssh " + username.getTextValue() + "@" + ingests.getIngestProcessor() + " kill -CONT "
				+ ingests.getId();
		log.info("PAUSE COMMAND : " + resumeCommand);
		final Integer resume = shellCommandRunner.executeForStatusCode(resumeCommand);
		if (resume == 0) {
			currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
			ingestsRepository.save(ingests);
		}
	}

	/**
	 * Kill Ingests on demand
	 *
	 * @param ingests
	 */
	public void stopIngest(Ingests ingests) {
		log.debug("Request to stop / kill ingest :{}", ingests);

		final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);
		final Lookups ingestScriptServer = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.JC_SERVER);
		final Lookups ingestTouchFileLocation = lookupsRepository.findByConfigAndKey(Constants.CONFIG,
				Constants.JC_QUEUED);

		final String sshCommand = "ssh " + username.getTextValue() + "@" + ingests.getIngestProcessor();

		final String removeTouchFileCommand = "ssh " + username.getTextValue() + "@" + ingestScriptServer.getTextValue()
				+ " rm " + ingestTouchFileLocation.getTextValue() + "/" + ingests.getPriority() + "." + ingests.getId();

		// remove touch file
		final Integer fileRemoved = shellCommandRunner.executeForStatusCode(removeTouchFileCommand);

		if (fileRemoved == 0) {
			// success
			ingests.setIngestCompletedTime(ZonedDateTime.now());
			ingests.setStatus(Constants.KILLED);
			currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
			ingestsRepository.save(ingests);
		} else {
			final String killCommand = sshCommand.concat(" kill -1 ").concat(ingests.getPid());

			final Integer killed = shellCommandRunner.executeForStatusCode(killCommand);
			if (killed == 0) {
				ingests.setIngestCompletedTime(ZonedDateTime.now());
				ingests.setStatus(Constants.KILLED);
				currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
				ingestsRepository.save(ingests);
				System.out.print("kill command : " + killCommand);
				// clear script execute
				final Lookups clearscript = lookupsRepository.findByConfigAndKey(Constants.CONFIG,
						Constants.INGEST_CLEANUP);
				final String cleanupCommand = sshCommand.concat(" ")
						.concat(clearscript.getTextValue().concat(" ") + (ingests.getId()));
				final Integer cleaned = shellCommandRunner.executeForStatusCode(cleanupCommand);

			}
		}

	}

	/**
	 * Get all running ingests from database
	 *
	 * @return
	 */
	public Set<Ingests> getAllRunningIngests() {

		Set<String> statuses = new HashSet<>();
		statuses.add(Constants.RUNNING);
		statuses.add(Constants.PAUSED);
		statuses.add(Constants.RESUME);
		statuses.add(Constants.QUEUED);

		final Set<Ingests> runningIngest = ingestsRepository.getByStatus(statuses);

		return runningIngest;
	}

	/**
	 * Get single ingest
	 *
	 * @param id
	 * @return
	 */
	public Ingests getIngest(Long id) {
		final Ingests ingests = ingestsRepository.findOne(id);
		return ingests;
	}

	/**
	 * To be implement ,delete ingest from start ingest itself
	 *
	 * @param id
	 */
	public void delete(Long id) {
		log.debug("Request to delete Ingest : {}", id);
		currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
		ingestsRepository.delete(id);

	}

	/**
	 * Save ingest into database
	 *
	 * @param ingests
	 * @return
	 */
	public Ingests save(Ingests ingests) {
		log.debug("Request to save Ingest");
		currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
		return ingestsRepository.save(ingests);
	}

	/**
	 * Get all ingests page by page
	 *
	 * @param pageable
	 * @return
	 */
	public Page<Ingests> findAll(Pageable pageable) {
		log.debug("Request to get all Ingest");
		currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);

		Page<Ingests> ingestses = ingestsRepository.findAll(pageable);

		return ingestses;
	}

	/**
	 * Search for the ingest corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Ingests> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of Ingests for query {}", query);
		return ingestsSearchRepository.search(queryStringQuery(query), pageable);
	}

	// Scan each folder for image count and compare with other folders for same
	// count
	private List<String> eachFolderImageCount(String[] params) {
		// params = ssh,title1/title2/new
		List<String> folderCount = new ArrayList<>();

		final String command = params[0].concat(" ls ").concat(params[1]);
		String warning = null;

		Process p2;
		try {
			p2 = Runtime.getRuntime().exec(command);
			final int status = p2.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String line = "";
			StringBuffer imagesPerFolder = new StringBuffer();
			Set<String> uniqueness = new HashSet<>();
			while ((line = reader.readLine()) != null) {

				final String command2 = params[0].concat(" find ").concat(params[1].concat("/").concat(line))
						.concat(" -type f -print | wc -l");
				String imageCount = shellCommandRunner.executeCommand(command2);

				final String totalFiles = imageCount.trim();
				imagesPerFolder.append(line + " : " + totalFiles + " images\n");

				folderCount.add(line + " : " + totalFiles + " images");

				uniqueness.add(totalFiles);
			}
			if (uniqueness.size() > 1) {
				warning = "Warning: Image Count not same";
			} else {
				warning = "";
			}
			folderCount.add(warning);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return folderCount;
	}

	/**
	 * LOGO'S
	 *
	 * @return
	 */
	public List<String> getLogos() {
		final Lookups logoLocation = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.LOGOS);
		final String command = "ls ".concat(logoLocation.getTextValue());
		final List<String> logos = shellCommandRunner.getFilesAndFolders(command);
		return logos;
	}

	/**
	 * Progress
	 *
	 * @param ingests
	 * @return
	 */
	public Ingests progress(Long id) {
		Ingests ingests = ingestsRepository.findOne(id);
		System.out.println("Inges print : " + ingests.toString());
		final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);
		final Lookups logFileLocation = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.JC_LOGFILE);
		final String logFileName = ingests.getId() + ".log";

		final String getCountCommand = "ssh " + username.getTextValue() + "@" + ingests.getIngestProcessor()
				+ " grep --regexp=\"$\" --count " + logFileLocation.getTextValue() + logFileName;
		System.out.println("file command : " + getCountCommand);

		final double totalDone = Double.parseDouble(shellCommandRunner.executeCommand(getCountCommand));

		ingests.setTotalDone(totalDone);
		currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
		return ingestsRepository.save(ingests);

		// wc -l < projectProgress.txt
		// file count

		// new command for line count
		// ssh root@10.0.1.105 grep --regexp="$" --count
		// /opt/smartlpc_ingests/JobControl/logs/84.log
		// return ingests;
	}

	/// create table for ingest servers and check for timestamp
	// get all servers with available values as 1.
	// for loop them and check for timestamp and idle time not more than 10mins
	/// or else declare them dead 0

	/**
	 * @return
	 */
	public Boolean idleservers() {

		final List<IngestServer> ingestServers = ingestServerRepository.findByAvailable(1);

		int count = 0;
		for (IngestServer ingestServer : ingestServers) {
			ZonedDateTime lastUsed = ingestServer.getLastUsed();
			ZonedDateTime currentTime = ZonedDateTime.now();

			long difference = datetimeDifference(lastUsed, currentTime, ChronoUnit.MINUTES);
			log.info("LAST USED TIMESTAMP : " + lastUsed);
			log.info(("CURRENT TIMESTAMP  : " + currentTime));
			log.info("CURRENT SERVER IDLE TIME MINUTES : " + difference);
			if (difference > 5) {
				log.info("Idle for more than 5 minutes");
			} else {
				count++;
				log.info("Perfect Server : " + ingestServer.getDns());
			}
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private static long datetimeDifference(ZonedDateTime z1, ZonedDateTime z2, ChronoUnit chronoUnit) {
		return chronoUnit.between(z1, z2);
	}

	// @Scheduled(cron="* 1 * * * *")
	public void doingSchedule() {
		log.info("Print at : " + new Date());
	}
}
