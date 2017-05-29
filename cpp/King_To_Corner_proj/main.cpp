//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: main.cpp
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 15-May-2017 17:02:19
//
// Include Files
#include "rt_nonfinite.h"
#include "King_To_Corner.h"
#include "main.h"
#include "King_To_Corner_terminate.h"
#include "King_To_Corner_initialize.h"

// Function Declarations
static void argInit_11x11_int8_T(signed char result[121]);
static signed char argInit_int8_T();
static void main_King_To_Corner();

// Function Definitions

//
// Arguments    : signed char result[121]
// Return Type  : void
//
static void argInit_11x11_int8_T(signed char result[121])
{
  int idx0;
  int idx1;

  // Loop over the array to initialize each element.
  for (idx0 = 0; idx0 < 11; idx0++) {
    for (idx1 = 0; idx1 < 11; idx1++) {
      // Set the value of the array element.
      // Change this value to the value that the application requires.
      result[idx0 + 11 * idx1] = argInit_int8_T();
    }
  }
}

//
// Arguments    : void
// Return Type  : signed char
//
static signed char argInit_int8_T()
{
  return 0;
}

//
// Arguments    : void
// Return Type  : void
//
static void main_King_To_Corner()
{
  signed char iv0[121];
  signed char move_to_corner[4];

  // Initialize function 'King_To_Corner' input arguments.
  // Initialize function input argument 'board'.
  // Call the entry-point 'King_To_Corner'.
  argInit_11x11_int8_T(iv0);
  King_To_Corner(iv0, argInit_int8_T(), argInit_int8_T(), move_to_corner);
}

//
// Arguments    : int argc
//                const char * const argv[]
// Return Type  : int
//
int javaCall()
{
  // Initialize the application.
  // You do not need to do this more than one time.
  King_To_Corner_initialize();

  // Invoke the entry-point functions.
  // You can call entry-point functions multiple times.
  main_King_To_Corner();

  // Terminate the application.
  // You do not need to do this more than one time.
  King_To_Corner_terminate();
  return 0;
}

//
// File trailer for main.cpp
//
// [EOF]
//
