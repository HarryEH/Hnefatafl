/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 * File: _coder_King_To_Corner_api.h
 *
 * MATLAB Coder version            : 3.3
 * C/C++ source code generated on  : 15-May-2017 17:02:19
 */

#ifndef _CODER_KING_TO_CORNER_API_H
#define _CODER_KING_TO_CORNER_API_H

/* Include Files */
#include "tmwtypes.h"
#include "mex.h"
#include "emlrt.h"
#include <stddef.h>
#include <stdlib.h>
#include "_coder_King_To_Corner_api.h"

/* Variable Declarations */
extern emlrtCTX emlrtRootTLSGlobal;
extern emlrtContext emlrtContextGlobal;

/* Function Declarations */
extern void King_To_Corner(int8_T board[121], int8_T x, int8_T y, int8_T
  move_to_corner[4]);
extern void King_To_Corner_api(const mxArray *prhs[3], const mxArray *plhs[1]);
extern void King_To_Corner_atexit(void);
extern void King_To_Corner_initialize(void);
extern void King_To_Corner_terminate(void);
extern void King_To_Corner_xil_terminate(void);

#endif

/*
 * File trailer for _coder_King_To_Corner_api.h
 *
 * [EOF]
 */
