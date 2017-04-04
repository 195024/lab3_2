package lab3_2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;

import static org.powermock.api.mockito.PowerMockito.*;
import org.mockito.internal.util.reflection.*;

import static org.hamcrest.CoreMatchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class })

public class TNewsLoader {

	private ConfigurationLoader configLoader = null;
	private NewsLoader newsLoader;

	@Before
	public void setup() {
		configLoader = PowerMockito.mock(ConfigurationLoader.class);
		PowerMockito.mockStatic(ConfigurationLoader.class);

		Configuration config = new Configuration();
		NewsReader newsReader = PowerMockito.mock(FileNewsReader.class);
		PowerMockito.mockStatic(NewsReaderFactory.class);
		IncomingNews incomingNews = new IncomingNews();

		newsLoader = new NewsLoader();

		Whitebox.setInternalState(config, "readerType", "tReader");

		incomingNews.add(new IncomingInfo("pub1", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("pub2", SubsciptionType.NONE));
		incomingNews.add(new IncomingInfo("sub1", SubsciptionType.A));
		incomingNews.add(new IncomingInfo("sub2", SubsciptionType.B));

		PowerMockito.when(ConfigurationLoader.getInstance()).thenReturn(configLoader);
		when(configLoader.loadConfiguration()).thenReturn(config);
		when(newsReader.read()).thenReturn(incomingNews);
		when(NewsReaderFactory.getReader("tReader")).thenReturn(newsReader);

	}

	@Test
	public void checkIfNewSizesAreCorrect() {
		PublishableNews publishableNews = newsLoader.loadNews();

		assertThat(publishableNews.getPublicContent().size(), is(equalTo(2)));
		assertThat(publishableNews.getSubscribentContent().size(), is(equalTo(2)));
	}
	
	@Test
	public void checkIfPublicContentIsCorrect() {
		PublishableNews publishableNews = newsLoader.loadNews();

		assertThat(publishableNews.getPublicContent().get(0), is(equalTo("pub1")));
		assertThat(publishableNews.getPublicContent().get(1), is(equalTo("pub2")));
	}
	
	@Test
	public void checkIfSubscribentContentIsCorrect() {
		PublishableNews publishableNews = newsLoader.loadNews();

		assertThat(publishableNews.getSubscribentContent().get(0), is(equalTo("sub1")));
		assertThat(publishableNews.getSubscribentContent().get(1), is(equalTo("sub2")));
	}
}