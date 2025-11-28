import { useState, useRef } from "react";
import { useQuery, useMutation } from "convex/react";
import { api } from "../../convex/_generated/api";

interface FeedPageProps {
  profile: any;
  user: any;
}

export default function FeedPage({ profile, user }: FeedPageProps) {
  const [content, setContent] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const imageInput = useRef<HTMLInputElement>(null);

  const posts = useQuery(api.posts.list) || [];
  const createPost = useMutation(api.posts.create);
  const generateUploadUrl = useMutation(api.posts.generateUploadUrl);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!content.trim() && !selectedImage) return;

    setIsSubmitting(true);
    try {
      let imageId = undefined;
      
      // Upload image if selected
      if (selectedImage) {
        const postUrl = await generateUploadUrl();
        const result = await fetch(postUrl, {
          method: "POST",
          headers: { "Content-Type": selectedImage.type },
          body: selectedImage,
        });
        const json = await result.json();
        if (!result.ok) {
          throw new Error(`Upload failed: ${JSON.stringify(json)}`);
        }
        imageId = json.storageId;
      }

      await createPost({ 
        content: content.trim() || "", 
        imageId 
      });
      
      setContent("");
      setSelectedImage(null);
      if (imageInput.current) {
        imageInput.current.value = "";
      }
    } catch (error) {
      console.error("Failed to create post:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setSelectedImage(file);
    }
  };

  return (
    <div className="space-y-6">
      {/* Post Creation */}
      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">
          What's on your mind?
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="Share something with the campus community..."
            rows={3}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
          />
          
          {/* Image Preview */}
          {selectedImage && (
            <div className="relative">
              <img
                src={URL.createObjectURL(selectedImage)}
                alt="Selected"
                className="max-w-xs max-h-48 rounded-lg border"
              />
              <button
                type="button"
                onClick={() => {
                  setSelectedImage(null);
                  if (imageInput.current) imageInput.current.value = "";
                }}
                className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-sm"
              >
                ×
              </button>
            </div>
          )}
          
          <div className="flex justify-end space-x-3">
            <input
              ref={imageInput}
              type="file"
              accept="image/*"
              onChange={handleImageSelect}
              className="hidden"
            />
            <button
              type="button"
              onClick={() => imageInput.current?.click()}
              className="px-4 py-2 text-blue-600 bg-blue-50 rounded-md hover:bg-blue-100"
            >
              📷 Add Photo
            </button>
            <button
              type="submit"
              disabled={isSubmitting || (!content.trim() && !selectedImage)}
              className="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 disabled:opacity-50"
            >
              {isSubmitting ? "Posting..." : "📝 Post"}
            </button>
          </div>
        </form>
      </div>

      {/* Posts Feed */}
      <div className="space-y-4">
        {posts.length === 0 ? (
          <div className="bg-white rounded-lg shadow p-8 text-center">
            <p className="text-gray-500">No posts yet. Be the first to share something!</p>
          </div>
        ) : (
          posts.map((post) => (
            <div key={post._id} className="bg-white rounded-lg shadow p-6">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="font-semibold text-gray-900">
                    {post.userName} {post.userRole === "teacher" ? "⭐" : ""}
                  </h3>
                  <p className="text-sm text-gray-500">
                    {new Date(post._creationTime).toLocaleDateString()} at{" "}
                    {new Date(post._creationTime).toLocaleTimeString()}
                  </p>
                </div>
              </div>
              {post.content && (
                <p className="text-gray-800 whitespace-pre-wrap mb-4">{post.content}</p>
              )}
              {post.imageUrl && (
                <img
                  src={post.imageUrl}
                  alt="Post image"
                  className="max-w-full h-auto rounded-lg border"
                />
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
