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
}

export const dateUtil = (dateString: string): DateUtil => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getUTCHours() % 24; // 이미 KST로 넘어오기때문에 UTC로 보정한 '척' 해야함.
  const minute = date.getMinutes();
  const second = date.getSeconds();
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const dayOfWeek = daysOfWeek[date.getDay()];
  const formattedDate = `${year}년 ${String(month).padStart(2, '0')}월 ${String(day).padStart(2, '0')}일 ${String(hour).padStart(2, '0')}시`;

  return {
    formattedDate,
    year,
    month,
    day,
    hour,
    minute,
    second,
    dayOfWeek,
  };
};
