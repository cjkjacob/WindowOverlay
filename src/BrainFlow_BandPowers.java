import brainflow.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class  BrainFlow_BandPowers
{
    public static double[] band_powers = new double[5];

    public static void main(String args[]){
        try {
            logging();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void logging () throws Exception
    {
        // use synthetic board for demo
        BoardShim.enable_board_logger (); // Enable board logger
        BrainFlowInputParams params = new BrainFlowInputParams (); // Get all the parameters for the synthetic board
        BoardIds board_id = BoardIds.SYNTHETIC_BOARD; // Using synthetic board
        BoardShim board_shim = new BoardShim (board_id, params); // Creating the board shim, an interface to use for the board with parameters and board type
        BoardDescr board_descr = BoardShim.get_board_descr (BoardDescr.class, board_id); // getting the description of the board
        int sampling_rate = board_descr.sampling_rate; // getting sampling rate of the board
        int nfft = DataFilter.get_nearest_power_of_two (sampling_rate); // getting nearest power of two
//        for (int j = 0; j < 10; j++) {

            board_shim.prepare_session(); // preparing session for stream
            board_shim.start_stream(3600); // start stream with 3600 bugger
            BoardShim.log_message(LogLevels.LEVEL_INFO, "Start sleeping in the main thread"); // log to console
            Thread.sleep(1500); // sleep for 10 seconds and collect info from stream
            board_shim.stop_stream(); // stop stream
            double[][] data = board_shim.get_board_data(); // collect data from stream
            board_shim.release_session(); // release session and flush data?

            List<Pair<double[], double[]>> pairs_psd = new ArrayList<Pair<double[], double[]>>(8);
            double[] band_power_alpha_channel_wise = new double[8];
            double[] band_power_beta_channel_wise = new double[8];
            double[] band_power_theta_channel_wise = new double[8];
            double[] band_power_delta_channel_wise = new double[8];
            double[] band_power_gamma_channel_wise = new double[8];

            for (int i = 1; i <= 8; i++) {
                DataFilter.detrend(data[i], DetrendOperations.LINEAR);
                pairs_psd.add(i - 1, DataFilter.get_psd_welch(data[i], nfft, nfft / 2, sampling_rate, WindowOperations.HANNING));
                band_power_alpha_channel_wise[i - 1] = DataFilter.get_band_power(pairs_psd.get(i - 1), 8.0, 12.0);
                band_power_beta_channel_wise[i - 1] = DataFilter.get_band_power(pairs_psd.get(i - 1), 12.0, 35.0);
                band_power_theta_channel_wise[i - 1] = DataFilter.get_band_power(pairs_psd.get(i - 1), 4.0, 8.0);
                band_power_delta_channel_wise[i - 1] = DataFilter.get_band_power(pairs_psd.get(i - 1), 0.5, 4.0);
                band_power_gamma_channel_wise[i - 1] = DataFilter.get_band_power(pairs_psd.get(i - 1), 35.0, 100.0);
            }

            DoubleSummaryStatistics dss_alpha = Arrays.stream(band_power_alpha_channel_wise).summaryStatistics();
            band_powers[0] = dss_alpha.getAverage();
            DoubleSummaryStatistics dss_beta = Arrays.stream(band_power_beta_channel_wise).summaryStatistics();
            band_powers[1] = dss_beta.getAverage();
            DoubleSummaryStatistics dss_theta = Arrays.stream(band_power_theta_channel_wise).summaryStatistics();
            band_powers[2] = dss_theta.getAverage();
            DoubleSummaryStatistics dss_delta = Arrays.stream(band_power_delta_channel_wise).summaryStatistics();
            band_powers[3] = dss_delta.getAverage();
            DoubleSummaryStatistics dss_gamma = Arrays.stream(band_power_gamma_channel_wise).summaryStatistics();
            band_powers[4] = dss_gamma.getAverage();

            System.out.println("Alpha band power average : " + band_powers[0]);
            System.out.println("Beta band power average : " + band_powers[1]);
            System.out.println("Theta band power average : " + band_powers[2]);
            System.out.println("Delta band power average : " + band_powers[3]);
            System.out.println("Gamma band power average : " + band_powers[4]);
//        }
    }
}