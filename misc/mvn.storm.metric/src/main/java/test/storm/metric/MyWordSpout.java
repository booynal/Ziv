package test.storm.metric;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.storm.metric.api.CountMetric;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ziv on 2017/8/21.
 */
public class MyWordSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MyWordSpout.class);
	private static final String[] words = new String[] { "hello", "world", "I", "love", "beijing" };

	private AtomicInteger seq = new AtomicInteger();
	private Random random = new Random();
	private SpoutOutputCollector collector;
	private CountMetric totalCountMetric; // 统计总共的单词数
	private AccumulatorCountMetric accumulatorCounter;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		LOGGER.info("Spout.declareOutputFields()");
		declarer.declare(new Fields("word"));
	}

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		LOGGER.info("Spout.open()");
		this.collector = collector;
		accumulatorCounter = new AccumulatorCountMetric();
		totalCountMetric = new CountMetric();
		context.registerMetric("spoutCountPer5", totalCountMetric, 5);
		context.registerMetric("spoutTotalCounter", accumulatorCounter, 1);
	}

	@Override
	public void activate() {
		LOGGER.info("Spout.activate()");
	}

	@Override
	public void nextTuple() {
		LOGGER.info("Spout.nextTuple()");
		Utils.sleep(1000);
		seq.incrementAndGet();
		String word = words[random.nextInt(words.length)];
		collector.emit(Arrays.asList(word), seq.get());
		accumulatorCounter.incr();
		totalCountMetric.incr();
		LOGGER.debug(String.format("MyWordSpout.emit: '%s'", word));
	}

	@Override
	public void ack(Object msgId) {
		LOGGER.info(String.format("Spout.ack(%s)", msgId));
	}

	@Override
	public void fail(Object msgId) {
		LOGGER.info(String.format("Spout.fail(%s)", msgId));
	}

	@Override
	public void deactivate() {
		LOGGER.info("Spout.deactivate()");
	}

	@Override
	public void close() {
		LOGGER.info("Spout.close()");
	}

}
