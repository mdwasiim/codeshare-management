export interface CSMServiceResponse<T> {
  success: boolean;
  data: T;
  error?: {
    code?: string;
    message?: string;
    details?: any;
  };
  transactionId: string;
  timestamp: string;
  timeTakenMs: number;
}
