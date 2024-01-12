package org.example;



import com.example.stick_hero.Game_Platform;

import java.awt.*;

public class Cherry extends Game_Platform {
    private int positionX;
    private int positionY;
    private int value;
    private Graphics g;

    public Cherry() {

    }

    // Getters
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getValue() {
        return value;
    }

    // Setters
    public void setPositionX() {

    }

    public void setPositionY() {

    }
  /*  #include <stdio.h>
#include <complex.h>
#include <stdlib.h>
#include <math.h>
#include "platform.h"
            #include "xil_printf.h"
            #include <xtime_l.h>
#include "xparameters.h"
            #include "xaxidma.h"
            #include "dma_init.h"

            #define N 32

            const int rev_32[N] = {0, 16, 8, 24, 4, 20, 12, 28, 2, 18, 10, 26, 6, 22, 14, 30, 1, 17, 9, 25, 5, 21, 13, 29, 3, 19, 11, 27, 7, 23, 15, 31};
const float complex W_32[N / 2] = {1.0 - 0.0 * I, 0.9807852804032304 - 0.19509032201612825 * I, 0.9238795325112867 - 0.3826834323650898 * I,
            0.8314696123025452 - 0.5555702330196022 * I, 0.7071067811865476 - 0.7071067811865475 * I,
            0.5555702330196023 - 0.8314696123025452 * I, 0.38268343236508984 - 0.9238795325112867 * I,
            0.19509032201612833 - 0.9807852804032304 * I, 0.0 - 1.0 * I, -0.1950903220161282 - 0.9807852804032304 * I,
            -0.3826834323650897 - 0.9238795325112867 * I, -0.555570233019602 - 0.8314696123025455 * I,
            -0.7071067811865475 - 0.7071067811865476 * I, -0.8314696123025453 - 0.5555702330196022 * I,
            -0.9238795325112867 - 0.3826834323650899 * I, -0.9807852804032304 - 0.1950903220161286 * I};

    void bitreverse(float complex dataIn[N], float complex dataOut[N]) {
        for (int i = 0; i < N; i++) {
            dataOut[i] = dataIn[rev_32[i]];
        }
    }

    void FFT_stages(float complex FFT_input[N], float complex FFT_output[N]) {
        for (int i = 0; i < N; i = i + 1) {
            FFT_input[i] = (sqrt(crealf(FFT_input[i]))) + (sqrt(cimagf(FFT_input[i]))) * I;
        }

        float complex temp1[N], temp2[N], temp3[N], temp4[N];

        for (int i = 0; i < N; i = i + 2) {
            temp1[i] = FFT_input[i] + FFT_input[i + 1];
            temp1[i + 1] = FFT_input[i] - FFT_input[i + 1];
        }

        for (int i = 0; i < N; i = i + 4) {
            for (int j = 0; j < 2; ++j) {
                temp2[i + j] = temp1[i + j] + W_32[8 * j] * temp1[i + j + 2];
                temp2[i + j + 2] = temp1[i + j] - W_32[8 * j] * temp1[i + j + 2];
            }
        }

        for (int i = 0; i < N; i = i + 8) {
            for (int j = 0; j < 4; ++j) {
                temp3[i + j] = temp2[i + j] + W_32[4 * j] * temp2[i + j + 4];
                temp3[i + j + 4] = temp2[i + j] - W_32[4 * j] * temp2[i + j + 4];
            }
        }

        for (int i = 0; i < N; i = i + 16) {
            for (int j = 0; j < 8; ++j) {
                temp4[i + j] = temp3[i + j] + W_32[2 * j] * temp3[i + j + 8];
                temp4[i + j + 8] = temp3[i + j] - W_32[2 * j] * temp3[i + j + 8];
            }
        }

        for (int i = 0; i < N / 2; i = i + 1) {
            FFT_output[i] = temp4[i] + W_32[i] * temp4[i + 16];
            FFT_output[i + 16] = temp4[i] - W_32[i] * temp4[i + 16];
        }
    }

    int main() {

        XTime PS_start_time;
        XTime PS_end_time;
        XTime PL_start_time;
        XTime PL_end_time;
        float time;
        int status;

    const float complex FFT_input[N] = {1 + 1 * I, 2 + 2 * I, 3 + 3 * I, 4 + 4 * I, 5 + 5 * I, 6 + 6 * I, 7 + 7 * I, 8 + 8 * I,
                9 + 9 * I, 10 + 10 * I, 11 + 11 * I, 12 + 12 * I, 13 + 13 * I, 14 + 14 * I, 15 + 15 * I,
                16 + 16 * I, 17 + 17 * I, 18 + 18 * I, 19 + 19 * I, 20 + 20 * I, 21 + 21 * I, 22 + 22 * I,
                23 + 23 * I, 24 + 24 * I, 25 + 25 * I, 26 + 26 * I, 27 + 27 * I, 28 + 28 * I, 29 + 29 * I,
                30 + 30 * I, 31 + 31 * I};
        float complex FFT_output_sw[N], FFT_output_hw[N];
        float complex FFT_rev[N];

        XTime_SetTime(0);
        XTime_GetTime(&PS_start_time);
        bitreverse(FFT_input, FFT_rev);
        FFT_stages(FFT_rev, FFT_output_sw);
        XTime_GetTime(&PS_end_time);

        XAxiDma AxiDMA;
        status = DMA_Init(&AxiDMA, XPAR_AXI_DMA_0_DEVICE_ID);
        if (status) {
            return 1;
        }
        XTime_SetTime(0);
        XTime_GetTime(&PL_start_time);
        status = XAxiDma_SimpleTransfer(&AxiDMA, (UINTPTR) FFT_output_hw, (sizeof(float complex) * N),
        XAXIDMA_DEVICE_TO_DMA);
        status = XAxiDma_SimpleTransfer(&AxiDMA, (UINTPTR) FFT_input, (sizeof(float complex) * N), XAXIDMA_DMA_TO_DEVICE);
        while (XAxiDma_Busy(&AxiDMA, XAXIDMA_DMA_TO_DEVICE)) {
            //printf("\n\rDMA-To-Device Transfer Done!");
        }
        while (XAxiDma_Busy(&AxiDMA, XAXIDMA_DEVICE_TO_DMA)) {
            //printf("\n\rDevice-To-DMA Transfer Done!");
        }
        XTime_GetTime(&PL_end_time);

        for (int i = 0; i < N; i++) {
            printf("\n\r%d.) PS Output: %f+%fI, PL Output: %f+%fI", (i + 1), crealf(FFT_output_sw[i]),
                    cimagf(FFT_output_sw[i]), crealf(FFT_output_hw[i]), cimagf(FFT_output_hw[i]));
            float diff1 = abs(crealf(FFT_output_sw[i]) - crealf(FFT_output_hw[i]));
            float diff2 = abs(cimagf(FFT_output_sw[i]) - cimagf(FFT_output_hw[i]));
            if (diff1 >= 0.001 && diff2 >= 0.001) {
                printf("\n\r    Data Mismatch found at index %d !", i);
            } else {
                printf("\r    DMA Transfer Successful!");
            }
        }

        printf("\n\r--------------------Execution Time Comparison--------------------");
        time = 0;
        time = (float) 1.0 * (PS_end_time - PS_start_time) / (COUNTS_PER_SECOND / 1000000);
        printf("\n\rExecution time for PS in Micro-seconds: %f", time);
        time = 0;
        time = (float) 1.0 * (PL_end_time - PL_start_time) / (COUNTS_PER_SECOND / 1000000);
        printf("\n\rExecution time for PL in Micro-seconds: %f", time);
        return 0;
    }*/

    public void setValue() {

    }


    public void draw() {

    }


}
