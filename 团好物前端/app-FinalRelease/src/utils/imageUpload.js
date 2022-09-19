import { doFilePost } from "./ajax"

export async function imageUpload(file) {
    let imageURL = await doFilePost("/tourist/doUpload", file);
    if (imageURL == "") imageUploadFail();

    return {
        url: imageURL,
    }
}

export function imageUploadFail() {
    throw new Error('Fail to upload')
}
