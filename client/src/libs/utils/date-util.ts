// DateUtil 타입 정의
export interface DateUtil {
  formattedDate: string;
  year: number;
  month: number;
  day: number;
  hour: number;
  minute: number;
  second: number;
  dayOfWeek: string;
  remainingDays: number;
}

export const dateUtil = (dateString: string): DateUtil => {
  const date = new Date(dateString);
  const now = new Date();

  const startOfToday = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const targetDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());

  const diffInMs = targetDate.getTime() - startOfToday.getTime();
  const remainingDays = Math.ceil(diffInMs / (1000 * 60 * 60 * 24));

  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getUTCHours() % 24; // 이미 KST로 넘어오기 때문에 UTC로 보정한 '척'
  const minute = date.getMinutes();
  const second = date.getSeconds();
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const dayOfWeek = daysOfWeek[date.getDay()];
  const formattedDate = `${year}년 ${String(month).padStart(2, '0')}월 ${String(day).padStart(2, '0')}일`;

  return {
    formattedDate,
    year,
    month,
    day,
    hour,
    minute,
    second,
    dayOfWeek,
    remainingDays,
  };
};

export const toLocalISOString = (date: Date): string => {
  const pad = (n: number) => n.toString().padStart(2, '0');

  const year = date.getFullYear();
  const month = pad(date.getMonth() + 1);
  const day = pad(date.getDate());
  const hours = pad(date.getHours());
  const minutes = pad(date.getMinutes());
  const seconds = pad(date.getSeconds());

  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
};
