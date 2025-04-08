# 이미지 관리 가이드

`fileService`를 사용하여 이미지를 업로드, 수정, 조회하는 방법을 설명합니다.

## 1. 이미지 업로드

새로운 이미지를 업로드할 때는 `uploadFile` 함수를 사용합니다.

```typescript
import { fileService } from '@/libs/services/file/fileService';

try {
  // file: File 객체 (input type="file"에서 받은 파일)
  // folder: 'classrooms' | 'missions' 중 하나 선택
  const imageKey = await fileService.uploadFile('classrooms', file);

  // imageKey를 서버에 저장하거나 필요한 곳에 사용
  // 예: "classrooms/302e3f79-083c-4e7c-adf7-a788d9b9c891.jpg"
} catch (error) {
  // 에러 처리
  console.error('이미지 업로드 실패:', error);
}
```

### 제한사항

- 허용된 파일 형식: jpg, jpeg, png
- 파일 형식이 맞지 않으면 에러가 발생합니다.

## 2. 이미지 수정

이미지를 수정할 때는 기존 이미지를 삭제하고 새로운 이미지를 업로드합니다.

```typescript
import { fileService } from '@/libs/services/file/fileService';

async function updateImage(oldImageKey: string, newFile: File) {
  try {
    // 1. 새 이미지 업로드
    const newImageKey = await fileService.uploadFile('classrooms', newFile);

    // 2. 서버 API를 통해 이미지 키 업데이트
    await updateImageKeyInServer(newImageKey);

    return newImageKey;
  } catch (error) {
    console.error('이미지 수정 실패:', error);
    throw error;
  }
}
```

## 3. 이미지 조회 (다운로드)

이미지를 화면에 표시할 때는 `getImageUrl` 함수를 사용합니다.

```typescript
import { fileService } from '@/libs/services/file/fileService';

// React 컴포넌트 예시
function ImageComponent({ imageKey }: { imageKey: string }) {
  const [imageUrl, setImageUrl] = useState<string>('');

  useEffect(() => {
    async function loadImage() {
      try {
        const url = await fileService.getImageUrl(imageKey);
        setImageUrl(url);
      } catch (error) {
        console.error('이미지 URL 가져오기 실패:', error);
      }
    }

    loadImage();
  }, [imageKey]);

  return <img src={imageUrl} alt="이미지" />;
}
```

### 이미지 URL 동작 방식

1. 서버에서 presigned URL을 받아옵니다.
2. 서버 요청 실패 시 S3 직접 URL로 폴백됩니다.
3. 이미지 키가 없는 경우 빈 문자열을 반환합니다.

## 주의사항

1. 이미지 키는 반드시 저장해두어야 합니다.
2. 이미지 URL은 임시 URL이므로, 컴포넌트가 마운트될 때마다 새로 받아와야 합니다.
3. 대용량 이미지 업로드 시 적절한 로딩 처리가 필요합니다.
